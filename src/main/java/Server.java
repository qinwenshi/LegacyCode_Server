import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;


public class Server {
	private boolean enableLogging = false;

	public static ExitSystem exitSystem = new ExitSystem();
	private static MyLogger logger = MyLogger.getInstance();

	private static Server server = new Server();

	public static ShouldLoop shouldLoop;

	public static ShouldLoop getShouldLoop(){
		if (shouldLoop == null)
			shouldLoop = new ShouldLoopImpl();
		return shouldLoop;
	}

	public Server() {
		this(false);
	}

	public Server(boolean enableDebugging) {
		enableLogging = enableDebugging;
	}

	public static void setShouldLoop(ShouldLoop shouldLoop){
		Server.shouldLoop = shouldLoop;
	}

	public static Server getServerInstance(){
		if(server == null)
			server = new Server();
		return server;
	}

	public static void setServerInstance(Server instance){
		server = instance;
	}

	public static void main(String args[]) throws Exception {
		if (args.length < 6) {
			System.err.println("Usage: java Server SMTPHost POP3Host user password EmailListFile CheckPeriodFromName");
			exitSystem.invoke();

		}

		String smtpHost = args[0],
				pop3Host = args[1],
				user = args[2],
				password = args[3],
				emailListFile = args[4],
				onBehalfOf = null;

		int checkPeriod = Integer.parseInt(args[5]);

		if (args.length > 6)
			onBehalfOf = args[6];

		Server ls = getServerInstance();

		logger = MyLogger.getInstance();
		while (getShouldLoop().shouldLoop()) {

			logger.log(new Date() + "> " + "SESSION START");

			ls.batchReplyMails(smtpHost, pop3Host, user, password, emailListFile, onBehalfOf);

			logger.log(new Date() + "> " + "SESSION END (Going to sleep for " + checkPeriod
						+ " minutes)");
			ls.waiting(checkPeriod);
		}
	}

	public void waiting(int checkPeriod) throws InterruptedException {
		Thread.sleep(checkPeriod * 1000 * 60);
	}

	public void batchReplyMails(String smtpHost, String pop3Host, String user, String password, String toEmailListFile, String onBehalfOf) throws IOException, MessagingException {
		PopMailbox popMailbox = new PopMailbox(pop3Host, user, password, enableLogging);
		popMailbox.openMailBox();

		if (popMailbox.mailBoxIsEmpty()) {
			return;
		}

		Message nextUnreadMessage;
		while((nextUnreadMessage = popMailbox.openNextUnreadMessage()) != null){
				new MailSender(smtpHost, user, password)
						.reply(nextUnreadMessage, onBehalfOf, toEmailListFile);

			popMailbox.deleteCurrentMessage();
		}
		popMailbox.closeFolder();
		popMailbox.closeMailBox();
	}
}

