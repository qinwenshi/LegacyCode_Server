import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by qinwenshi on 8/8/16.
 */
public class MailSender {

    private String smtphost;
    private String username;
    private String mailPassword;

    private static final String SMTP_MAIL = "smtp";

    public MailSender( String smtphost, String username, String mailPassword) {
        this.smtphost = smtphost;
        this.username = username;
        this.mailPassword = mailPassword;

    }

    public void reply(Message originalMessage, String onBehalfOf, String recipientsListFile) throws MessagingException, IOException {
        Properties props = new Properties();

        props.put("mail.smtp.host", smtphost);
        Session session1 = Session.getDefaultInstance(props, null);
        Mail mail = new Mail(originalMessage, onBehalfOf, username );

        // Send newMessage
        //
        Transport transport = session1.getTransport(SMTP_MAIL);

        transport.connect(smtphost, username, mailPassword);
        Message newMessage = mail.composeNewMessage(session1, recipientsListFile);
        transport.sendMessage(newMessage, newMessage.getRecipients(Message.RecipientType.BCC));
    }
}
