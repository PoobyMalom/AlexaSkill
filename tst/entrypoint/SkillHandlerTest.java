package entrypoint;


import com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers.GetBookLength;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dao.DynamoDao;

import java.util.List;
import java.util.Map;

public class SkillHandlerTest {


    private GetBookLength handler;

    final AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-1").build();

    /**
     * This is called before each test.  Do shared setup work here.
     * @throws Exception
     */
    @org.junit.Before // this annotation makes this method run before each test
    public void setUp() throws Exception {
         handler = new GetBookLength();
    }

    @org.junit.Test // same, this is a test
    public void handle() throws Exception {
        System.out.println(handler.searchBook("words of radiance"));
        //handler.retrieveBooksInReadingList();
    }
}
