import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import pl.marchwicki.junitcharacterization.CharacterizationBuilder;
import pl.marchwicki.junitcharacterization.CharacterizationRule;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static pl.marchwicki.junitcharacterization.CharacterizationRule.aRuleFor;

/**
 * Created by qinwenshi on 8/5/16.
 */
@RunWith(JUnitParamsRunner.class)
public class TestLogger {

    private class SendMailServerStub extends Server{

        @Override
        public void batchReplyMails(String smtpHost, String pop3Host, String user, String password, String toEmailListFile, String onBehalfOf) throws IOException, MessagingException {
            System.out.println("mail sent");
        }

        @Override
        public void waiting(int checkPeriod) throws InterruptedException {
            System.out.println("Waiting for "+ checkPeriod);
        }
    }

    class ExitSystemMock extends ExitSystem {
        @Override
        protected void invoke() throws Exception {
            System.out.println("Exiting System with status 1");
            throw new Exception("System exiting with status 1");
        }
    }

    class LoopOnce implements ShouldLoop{
        private int remainingLoop = 1;

        public boolean shouldLoop() {
            return remainingLoop-- > 0;

        }
    }



    final static String DEFAULT_FOLDER = System.getProperty("java.io.tmpdir");
    final static String DEFAULT_FILENAME = TestLogger.class.getCanonicalName() + ".txt";

    @ClassRule
    public static CharacterizationRule rule = aRuleFor(TestLogger.class)
            .withRules()
            .clearOutputBeforeCapture()
            .inFolder(DEFAULT_FOLDER)
            .withFilename(DEFAULT_FILENAME)
            .up()
            .build();

    private Server server ;

    private ExitSystemMock exitSystemMock = new ExitSystemMock();

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void setup(){
        server = new SendMailServerStub();

        Server.exitSystem = exitSystemMock;
        Server.setServerInstance(server);
        Server.setShouldLoop(new LoopOnce());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        System.setProperty(CharacterizationBuilder.ENV_NAME_FOR_RECORDING, "true");
    }

    @Test
    @FileParameters("classpath:parametersForLogger.csv")
    public void run_send_mail(String[] parameters) {
        try{
            server.main(parameters);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}



