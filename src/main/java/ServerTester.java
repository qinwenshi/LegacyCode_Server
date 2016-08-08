import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.marchwicki.junitcharacterization.CharacterizationBuilder;
import pl.marchwicki.junitcharacterization.CharacterizationRule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static pl.marchwicki.junitcharacterization.CharacterizationRule.aRuleFor;

/**
 * Created by qinwenshi on 8/4/16.
 */
@RunWith(JUnitParamsRunner.class)
public class ServerTester {


    final static String DEFAULT_FOLDER = System.getProperty("java.io.tmpdir");
    final static String DEFAULT_FILENAME = ServerTester.class.getCanonicalName() + ".txt";
    final static String MESSAGES_FOLDER = "/Users/qinwenshi/Desktop/fakeSMTP/messages";
    @ClassRule
    public static CharacterizationRule rule = aRuleFor(ServerTester.class)
            .withRules()
            .clearOutputBeforeCapture()
            .inFolder(DEFAULT_FOLDER)
            .withFilename(DEFAULT_FILENAME)
            .up()
            .build();

    private Server server ;
    private int sentMessageCount = 0;
    @Before
    public void setup(){
        server = new Server();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        System.setProperty(CharacterizationBuilder.ENV_NAME_FOR_RECORDING, "true");
        sentMessageCount = getSentMessageCount();
    }

    @Test
    @FileParameters("classpath:with6Params.csv")
    public void run_send_mail(String[] parameters) throws Exception {
        server.batchReplyMails(parameters[0], parameters[1],parameters[2],parameters[3],parameters[4],parameters[5]);
        assertEquals(sentMessageCount+1, getSentMessageCount());
    }

    private int getSentMessageCount(){
        return new File(MESSAGES_FOLDER).list().length;
    }

}

