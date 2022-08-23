package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class GetBookRatingIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GetRatingIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Map<String, Slot> slots = getSlots(input);

        StringBuilder speechText = new StringBuilder();

        GetRating getRating = new GetRating();
        String rating = getRating.searchBook(slots.get("RatingBookNameSlot").getValue());

        if (slots.containsKey("RatingBookNameSlot") && null != slots.get("RatingBookNameSlot").getValue()) {
            if (rating != null) {
                speechText.append(slots.get("RatingBookName").getValue()).append(" Has a Rating of ").append(rating);
            }
            else {
                speechText.append("cannot find the rating of the book");
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
