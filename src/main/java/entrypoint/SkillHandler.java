package entrypoint;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers.*;

import java.awt.print.Book;

/**
 * This skill is the entry point the skill
 */
public class SkillHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        // Handlers that are in com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers
                        // most of these are boilerplate.  The big one we care about is HelloWorldIntentHandler
                        new CancelandStopIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler(),
                        new BookRemoveIntentHandler(),
                        new AddBookIntentHandler(),
                        new GetBookListIntentHandler(),
                        new GetBookListsIntentHandler(),
                        new GetBookRatingIntentHandler(),
                        new RemoveListIntentHandler())
                // Add your skill id below if you want? Not sure why.
                //.withSkillId("")
                .build();
    }

    /**
     * Luckily a lot of this logic is written for us in a class called SkillStreamHandler
     * We just extend it (https://www.tutorialspoint.com/java/java_inheritance.htm)
     */
    public SkillHandler() {
        super(getSkill());
    }

}
