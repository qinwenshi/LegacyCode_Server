/**
 * Created by qinwenshi on 8/8/16.
 */
public class MyLogger {
    private final boolean debugOn;
    private static MyLogger logger;
    public MyLogger(boolean enableLogging) {
        debugOn = enableLogging;
    }

    public static MyLogger getInstance(){
        if(logger == null)
            logger = new MyLogger(false);
        return logger;
    }

    public void log(String loggingMessage) {
        if(debugOn)
            System.out.println(loggingMessage);
    }
}
