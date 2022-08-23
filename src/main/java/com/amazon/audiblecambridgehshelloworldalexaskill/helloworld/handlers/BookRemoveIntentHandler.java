package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class BookRemoveIntentHandler implements RequestHandler {

    public final String textWithBook = "<speak> %s removed from your reading list </speak>"; //TODO add list name
    public final String textWithoutBook = "<speak> cannot find %s in your reading list </speak>";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("RemoveBookIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Map<String, Slot> slots = getSlots(input);

        String speechText;

        if (slots.containsKey("RemoveBookSlot") && null != slots.get("RemoveBookSlot").getValue()) {
            speechText = String.format(textWithBook, slots.get("RemoveBookSlot").getValue());
        }
        else {
            speechText = String.format(textWithoutBook, slots.get("RemoveBookSlot").getValue());
        }

        return input.getResponseBuilder()
                .withSpeech(speechText) // alexa says this
                .withSimpleCard("Removed", speechText) // alexa will show this on a screen
                .build();
    }

    Map<String, Slot> getSlots(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return Collections.unmodifiableMap(intent.getSlots());
    }
}
