package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dao.DynamoDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class GetBookListsIntentHandler implements RequestHandler {
    final AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    final DynamoDao dynamoDao = new DynamoDao(dynamoDb);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GetBookListsIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Map<String, Slot> slots = getSlots(input);

        List<Map<String, AttributeValue>> maps = dynamoDao.retrieveListsInReadingLists(slots.get("GetListNameSlot").getValue());

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

    Map<String, Slot> getSlots(HandlerInput input) {
        // this chunk of code gets the slots
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return Collections.unmodifiableMap(intent.getSlots());
    }

}
