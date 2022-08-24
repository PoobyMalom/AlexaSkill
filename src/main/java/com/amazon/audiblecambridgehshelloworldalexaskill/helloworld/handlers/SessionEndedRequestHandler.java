package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import java.util.Date;
import java.util.Optional;
import static com.amazon.ask.request.Predicates.requestType;
public class SessionEndedRequestHandler implements RequestHandler {
    //private final String endOfProgram = "<speak> Closing Program </speak>";
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(SessionEndedRequest.class));
    }
    @Override
    public Optional<Response> handle(HandlerInput input) {
        // any cleanup logic goes here
        log(input,"Request: "+input.getRequestEnvelope().getRequest());
        return input.getResponseBuilder().build();
    }
    void log(HandlerInput input, String message) {
        System.out.printf("[%s] [%s] : %s]\n",
                input.getRequestEnvelope().getRequest().getRequestId(),
                new Date(),
                message);
    }
}
