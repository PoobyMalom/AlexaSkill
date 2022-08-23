package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.AttributeTransformer;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DynamoDao {
    /**
     * dynamo client
     */
    private final AmazonDynamoDB dynamoDb;

    // Table we juts created
    private static final String TABLE_NAME = "book_lists";

    // Partition key we set.
    private static final String BOOK_NAME = "book_name";

    private static final String LIST_NAME = "list_name";


    public DynamoDao(AmazonDynamoDB dynamoDb) {

        this.dynamoDb = dynamoDb;
    }

    // Write to Dynamo DB
    public boolean addBookToReadingList(String bookName, String listName) {
        try {

            Map<String, AttributeValue> itemAttributesMap = new HashMap<>();
            itemAttributesMap.put(BOOK_NAME, new AttributeValue().withS(bookName.toLowerCase()));
            itemAttributesMap.put(LIST_NAME, new AttributeValue().withS(listName.toLowerCase()));


            PutItemRequest putItemRequest = new PutItemRequest()
                    .withTableName(TABLE_NAME)
                    .withItem(itemAttributesMap);

            dynamoDb.putItem(putItemRequest);
            return true;
        } catch (Exception e) {

            return false;
        }
    }


    // read from Dynamo DB
    public List<Map<String, AttributeValue>> retrieveBooksInReadingList() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(TABLE_NAME)
                // prevent timeout and extra costs
                .withLimit(100);
        final ScanResult scanResult = dynamoDb.scan(scanRequest);
        List<Map<String, AttributeValue>> items = scanResult.getItems();
        return items;
    }

    public List<Map<String, AttributeValue>> retrieveListsInReadingLists(String listName) {

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#List_name", "list_name");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":list", new AttributeValue().withS(listName));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("#List_name = :list")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues);

        QueryResult queryResult = dynamoDb.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        return items;

    }

    public boolean deleteReadingList(String listName) {
        try {
            Map<String, AttributeValue> itemAttributesMapBooks = new HashMap<>();
            itemAttributesMapBooks.put(BOOK_NAME, new AttributeValue().withS(listName));

            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(TABLE_NAME)
                    .withExclusiveStartKey(itemAttributesMapBooks)
                        // prevent timeout and extra costs
                    .withLimit(100);

            final ScanResult scanResult = dynamoDb.scan(scanRequest);
            List<Map<String, AttributeValue>> items = scanResult.getItems();
            for (Map<String, AttributeValue> item : items) {
                DeleteItemRequest deleteList = new DeleteItemRequest()
                        .withTableName(TABLE_NAME)
                        .withKey(item)
                        ;

                System.out.println(deleteList);
                dynamoDb.deleteItem(listName, item);
            }

            Map<String, AttributeValue> itemAttributesMap = new HashMap<>();
            itemAttributesMap.put(LIST_NAME, new AttributeValue().withS(listName));

            System.out.println("done");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
