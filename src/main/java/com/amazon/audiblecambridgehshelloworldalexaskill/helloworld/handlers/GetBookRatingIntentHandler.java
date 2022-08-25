package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class GetBookRatingIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GetBookRatingIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Map<String, Slot> slots = getSlots(input);

        StringBuilder speechText = new StringBuilder();

        GetRating getRating = new GetRating();
        String rating = getRating.searchBook(slots.get("RatingBookNameSlot").getValue());
        log(input, rating);

        if (slots.containsKey("RatingBookNameSlot") && null != slots.get("RatingBookNameSlot").getValue()) {
            if (rating != null) {
                speechText.append(slots.get("RatingBookNameSlot").getValue()).append(" Has a Rating of ").append(rating);
                log(input, "added message");
            }
            else {
                speechText.append("cannot find the rating of the book");
            }
        }

        return input.getResponseBuilder()
                .withSpeech(speechText.toString()) // alexa says this
                .withSimpleCard(slots.get("RatingBookNameSlot").getValue() + " Rating",
                        speechText.substring(7, speechText.length()-8)) // alexa will show this on a screen
                .build();
    }

    Map<String, Slot> getSlots(HandlerInput input) {
        // this chunk of code gets the slots
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return Collections.unmodifiableMap(intent.getSlots());
    }



    /**
     * Logs debug messages in an easier to search way
     * You can also use system. Out, but it'll be harder to work with
     */
    void log(HandlerInput input, String message) {
        System.out.printf("[%s] [%s] : %s]\n",
                input.getRequestEnvelope().getRequest().getRequestId(),
                new Date(),
                message);
    }
}
