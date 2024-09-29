package seng202.team1;

import seng202.team1.gui.FXWindow;

import java.io.IOException;

/**
 * Default entry point class.
 * @author seng202 teaching team
 */
public class App {

    /**
     * Entry point which runs the javaFX application.
     * Also shows off some different logging levels
     * @param args program arguments from command line
     * @throws IOException when something goes wrong with the input/output
     */
    public static void main(String[] args) throws IOException {
        FXWindow.launchWrapper(args);
    }
}
