package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
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
    public void addBookToReadingList(String bookName, String listName) {
        try {

            Map<String, AttributeValue> itemAttributesMap = new HashMap<>();
            itemAttributesMap.put(BOOK_NAME, new AttributeValue().withS(bookName));
            itemAttributesMap.put(LIST_NAME, new AttributeValue().withS(listName));


            PutItemRequest putItemRequest = new PutItemRequest()
                    .withTableName(TABLE_NAME)
                    .withItem(itemAttributesMap);

            dynamoDb.putItem(putItemRequest);
        } catch (Exception ignored) {

        }
    }


    // read from Dynamo DB
    public List<Map<String, AttributeValue>> retrieveBooksInReadingList() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(TABLE_NAME)
                // prevent timeout and extra costs
                .withLimit(100);
        final ScanResult scanResult = dynamoDb.scan(scanRequest);
        return scanResult.getItems();
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
        return queryResult.getItems();

    }

    /*
    public void deleteReadingList(String listName) {
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
                        .withKey(item);

                System.out.println(deleteList);
                dynamoDb.deleteItem(listName.toLowerCase(), item);
            }

            Map<String, AttributeValue> itemAttributesMap = new HashMap<>();
            itemAttributesMap.put(LIST_NAME, new AttributeValue().withS(listName));

            System.out.println("done");
        }
        catch (Exception ignored) {
        }
    }
     */

    public void removeBookFromReadingList(Map<String, AttributeValue> item) {
        DeleteItemRequest deleteReq = new DeleteItemRequest()
                .withTableName(TABLE_NAME)
                .withKey(item);

        try {
            System.out.println(item.toString());

            dynamoDb.deleteItem(deleteReq);
            System.out.println("deleted");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void clearList(String list)
    {
        try {
            List<Map<String, AttributeValue>> bookList;
            bookList = retrieveListsInReadingLists(list);
            System.out.println("books retrieved");
            bookList.forEach(item -> {
                System.out.println("entered for loop");
                System.out.println(item);
                removeBookFromReadingList(item);
                System.out.println("removed, end for loop");
            });

            System.out.println("out of for loop, returns true");

        }
        catch (Exception e)
        {
            System.out.println("error: " + e);
        }
    }

}
