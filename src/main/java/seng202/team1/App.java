package seng202.team1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWindow;

import java.io.IOException;

/**
 * Default entry point class.
 * @author seng202 teaching team
 */
public class App {
    private static final Logger LOG = LogManager.getLogger(App.class);

    /**
     * Entry point which runs the javaFX application.
     * Also shows off some different logging levels
     * @param args program arguments from command line
     * @throws IOException when something goes wrong with the input/output
     */
    // TODO remove exception since its never thrown
    public static void main(String[] args)
    {
        LOG.info("WineTime has been launched!");

        FXWindow.launchWrapper(args);
    }
}