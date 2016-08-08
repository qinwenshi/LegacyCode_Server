import javax.mail.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by qinwenshi on 8/8/16.
 */
class PopMailbox {
    private static final String INBOX = "INBOX", POP_MAIL = "pop3";
    private String pop3Host;
    private String username;
    private String mailPassword;
    private boolean debugOn;
    private Store store;
    private Folder folder;
    private Message[] allMessages;
    private Message currentMessage;
    private int currentReadPosition = 0;

    public PopMailbox(String pop3Host, String username, String mailPassword, boolean debugOn) {
        this.pop3Host = pop3Host;
        this.username = username;
        this.mailPassword = mailPassword;
        this.debugOn = debugOn;
    }

    public Store getStore() {
        return store;
    }

    public Folder getFolder() {
        return folder;
    }


    public boolean mailBoxIsEmpty() throws MessagingException {
        boolean isEmpty = false;
        int totalMessages = folder.getMessageCount();

        if (totalMessages == 0) {
            MyLogger.getInstance().log(new Date() + "> " + folder + " is empty");

            folder.close(false);
            isEmpty = true;
        }
        return isEmpty;
    }

    public Message[] openDefaultFolder() throws MessagingException {
        Message[] messages = folder.getMessages();
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(FetchProfile.Item.FLAGS);
        fp.add("X-Mailer");
        folder.fetch(messages, fp);
        return messages;
    }

    public PopMailbox openMailBox() throws MessagingException {
        // Get a Session object
        //
        Properties sysProperties = System.getProperties();
        Session session = Session.getDefaultInstance(sysProperties, null);
        session.setDebug(debugOn);

        // Connect to host
        //
        store = session.getStore(POP_MAIL);
        store.connect(pop3Host, -1, username, mailPassword);

        // Open the default folder
        //
        folder = store.getDefaultFolder();
        if (folder == null)
            throw new NullPointerException("No default mail folder");

        folder = folder.getFolder(INBOX);
        if (folder == null)
            throw new NullPointerException("Unable to get folder: " + folder);

        // Get message count
        //
        folder.open(Folder.READ_WRITE);
        return this;
    }

    public void closeMailBox() throws MessagingException {
        store.close();
    }

    public void closeFolder() throws MessagingException {
        folder.close(true);
    }

    public Message openNextUnreadMessage() throws MessagingException {
        if(allMessages == null)
            allMessages = openDefaultFolder();
        while(currentReadPosition < allMessages.length) {
            if(!allMessages[currentReadPosition].isSet(Flags.Flag.SEEN)){
                currentMessage = allMessages[currentReadPosition++];
                return currentMessage;
            }
            currentReadPosition++;
        }
        resetCursor();
        return null;
    }

    private void resetCursor() {
        currentReadPosition = 0;
        currentMessage = null;
    }

    public void deleteCurrentMessage() throws MessagingException {
        currentMessage.setFlag(Flags.Flag.DELETED, true);
    }
}
