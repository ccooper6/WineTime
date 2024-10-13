package seng202.team1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWindow;

/**
 * Default entry point class.
 */
public class App {
    private static final Logger LOG = LogManager.getLogger(App.class);

    /**
     * Default constructor for App
     */
    public App(){}

    /**
     * Entry point which runs the javaFX application.
     * Also shows off some different logging levels
     * @param args program arguments from command line
     */
    public static void main(String[] args)
    {
        LOG.info("WineTime has been launched!");

        FXWindow.launchWrapper(args);
    }
}