package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazon.ask.model.Slot;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dao.DynamoDao;


import java.awt.print.Book;
import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class AddBookIntentHandler implements RequestHandler {

    private final String speechTextWithBook = "<speak> Added %s to your %s </speak>";
    private final String speechTextNoBookName = "No book given";

    final AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    final DynamoDao dynamoDao = new DynamoDao(dynamoDb);



    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("BookListIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {

        log(handlerInput, "Starting request");
        logSlots(handlerInput);


        Map<String, Slot> slots = getSlots(handlerInput);

        String speechText;

        GetAuthor authorObj = new GetAuthor();
        String author = authorObj.searchBook(slots.get("BookNameSlot").getValue());

        if(slots.containsKey("BookNameSlot") && null != slots.get("BookNameSlot").getValue()) {
            //add to book database later
            if(slots.containsKey("ListNameSlot") && null != slots.get("ListNameSlot").getValue()) {
                dynamoDao.addBookToReadingList(slots.get("BookNameSlot").getValue() + " By " + author,
                        slots.get("ListNameSlot").getValue());
                speechText = String.format(speechTextWithBook, slots.get("BookNameSlot").getValue() + " by " + author,
                        slots.get("ListNameSlot").getValue());
            }
            else {
                dynamoDao.addBookToReadingList(slots.get("BookNameSlot").getValue() + " by " + author,
                        "reading list");
                speechText = String.format(speechTextWithBook, slots.get("BookNameSlot").getValue() + " by " + author,
                        "reading list");
            }


        } else {
            speechText = speechTextNoBookName;
        }





        List<Map<String, AttributeValue>> maps = dynamoDao.retrieveBooksInReadingList();
        log(handlerInput, "DynamoDb responses " + maps);

        String key = "BOOKNAME";
        String value = slots.get("BookNameSlot").getValue();

        log(handlerInput, "Speech text response is " + speechText);

        // response object with a card (shown on devices with a screen) and speech (what alexa says)
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText) // alexa says this
                .withSimpleCard("HelloWorld", speechText) // alexa will show this on a screen
                .build();
    }

    Map<String, Slot> getSlots(HandlerInput input) {
        // this chunk of code gets the slots
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return Collections.unmodifiableMap(intent.getSlots());
    }

    void logSlots(HandlerInput input) {
        Map<String, Slot> slots = getSlots(input);
        // log slot values including request id and time for debugging
        for(String key : slots.keySet()) {
            log(input, String.format("Slot value key=%s, value = %s", key, slots.get(key).toString()));
        }
    }

    /**
     * Logs debug messages in an easier to search way
     * You can also use system. Out, but it'll be harder to work with
     */
    void log(HandlerInput input, String message) {
        System.out.printf("[%s] [%s] : %s]\n",
                input.getRequestEnvelope().getRequest().getRequestId().toString(),
                new Date(),
                message);
    }
}
