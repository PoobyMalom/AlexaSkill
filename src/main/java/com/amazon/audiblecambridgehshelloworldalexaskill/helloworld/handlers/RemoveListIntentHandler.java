package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import dao.DynamoDao;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class RemoveListIntentHandler implements RequestHandler{
    public final String textWithBook = "<speak> %s successfully deleted </speak>"; //TODO add list name

    final AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();

    final DynamoDao dynamoDao = new DynamoDao(dynamoDb);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("RemoveListIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Map<String, Slot> slots = getSlots(input);

        String speechText;



        if (slots.containsKey("RemoveListSlot") && null != slots.get("RemoveListSlot").getValue()) {
            speechText = String.format(textWithBook, slots.get("RemoveListSlot").getValue());
            dynamoDao.clearList(slots.get("RemoveListSlot").getValue());
        }
        else {
            speechText = "Reading List successfully Deleted";
            dynamoDao.clearList("reading list");
        }

        return input.getResponseBuilder()
                .withSpeech(speechText) // alexa says this
                .withSimpleCard("Removed",  speechText.substring(7, speechText.length()-8)) // alexa will show this on a screen
                .build();
    }

    Map<String, Slot> getSlots(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return Collections.unmodifiableMap(intent.getSlots());
    }
}
