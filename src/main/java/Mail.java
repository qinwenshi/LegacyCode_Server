import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

/**
 * Created by qinwenshi on 8/8/16.
 */
public class Mail {
    private Message message;
    private String replyTo;
    private String subject;
    private Date sentDate;
    private String onBehalfOf;
    private String userName;

    public Mail(Message message, String from, String userName){
        this.message = message;

        this.onBehalfOf = from;
        this.userName = userName;
    }

    public Message composeNewMessage(Session session, String emailListFile) throws MessagingException, IOException {
        replyTo = userName;
        String xMailer;
        String messageText;
        int size;
        Address[] a = null;

        InternetAddress[] recipientList = loadRecipientList(emailListFile);

        Address replyToList[] = {new InternetAddress(replyTo)};

        // Get Headers (from, to, subject, date, etc.)
        //
        if ((a = message.getFrom()) != null)
            replyTo = a[0].toString();

        subject = message.getSubject();
        sentDate = message.getSentDate();
        size = message.getSize();
        String[] hdrs = message.getHeader("X-Mailer");
        if (hdrs != null)
            xMailer = hdrs[0];
        onBehalfOf = userName;

        recipientList = loadRecipientList(emailListFile);

        Message newMessage = new MimeMessage(session);

        if (onBehalfOf != null)
            newMessage.setFrom(new InternetAddress(onBehalfOf, onBehalfOf
                    + " on behalf of " + replyTo));
        else
            newMessage.setFrom(new InternetAddress(onBehalfOf));
        newMessage.setReplyTo(replyToList);

        newMessage.setRecipients(Message.RecipientType.BCC, recipientList);
        newMessage.setSubject(subject);
        newMessage.setSentDate(sentDate);

        // Set message contents
        //
        Object content = message.getContent();
        String debugText = "Subject: " + subject + ", Sent date: " + sentDate;

        if (content instanceof Multipart) {
            MyLogger.getInstance().log(new Date() + "> " + "Sending Multipart message (" + debugText + ")");

            newMessage.setContent((Multipart) message.getContent());
        } else {
            MyLogger.getInstance().log(new Date() + "> " + "Sending Text message (" + debugText + ")");

            newMessage.setText((String) content);
        }
        Template template = new Template();

        newMessage.setText(template.make());
        return newMessage;
    }


    private InternetAddress[] loadRecipientList(String emailListFile) throws IOException, AddressException {
        InternetAddress[] recipientList;// Read in email list file into java.util.Vector
        //
        Vector vList = new Vector(10);
        BufferedReader listFile = new BufferedReader(new FileReader(
                emailListFile));
        String line = null;
        while ((line = listFile.readLine()) != null) {
            vList.addElement(new InternetAddress(line));
        }
        listFile.close();

        MyLogger.getInstance().log(new Date() + "> " + "Found " + vList.size() + " email ids in list");


        recipientList = new InternetAddress[vList.size()];
        vList.copyInto(recipientList);
        vList = null;

        //
        return recipientList;
    }


}
