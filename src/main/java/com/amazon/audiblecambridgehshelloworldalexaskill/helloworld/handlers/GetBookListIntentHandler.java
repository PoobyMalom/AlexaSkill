package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dao.DynamoDao;


import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class GetBookListIntentHandler implements RequestHandler {


    final AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    final DynamoDao dynamoDao = new DynamoDao(dynamoDb);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GetBookListIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        List<Map<String, AttributeValue>> maps = dynamoDao.retrieveBooksInReadingList();

        StringBuilder speechText = new StringBuilder();

        for (Map<String, AttributeValue> map: maps) {
            for (AttributeValue book : map.values()) {
                String text = book.getS();
                speechText.append(" ").append(text);
            }
        }

        return input.getResponseBuilder()
                .withSpeech(speechText.toString()) // alexa says this
                .withSimpleCard("Removed", speechText.toString()) // alexa will show this on a screen
                .build();
    }
}
