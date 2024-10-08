package seng202.team1.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Service class for the wine variety feature.
 * @author Isaac Macdonald, Wen Sheng Thong
 */
public class WineVarietyService {
    private static final HashSet<String> reds = new HashSet<>();
    private static final HashSet<String> whites = new HashSet<>();
    private static final HashSet<String> rose = new HashSet<>();
    private static final HashSet<String> sparkling = new HashSet<>();
    private static WineVarietyService instance;

    /**
     * gets the set of red wines
     */
    public HashSet<String> getReds() {
        return reds;
    }

    /**
     * gets the set of white wines
     */
    public HashSet<String> getWhites() {
        return whites;
    }

    /**
     * gets the set of rose wines
     */
    public HashSet<String> getRose() {
        return rose;
    }

    /**
     * gets the set of sparkling wines
     */
    public HashSet<String> getSparkling() {
        return sparkling;
    }

    /**
     Returns the instance and creates one if none exists.
     @return {@link WineVarietyService instance}
     */
    public static WineVarietyService getInstance() {
        if (instance == null) {
            instance = new WineVarietyService();


        }
        return instance;
    }

    /**
     * The initialiser for WineVarietyService, calls setVarietySetsFromTextFiles.
     */
    public WineVarietyService() {
        setVarietySetsFromTextFiles();
    }

    /**
     * This method takes all the wine varietys in the text files and puts them in sets in WineVarietyService.
     */
    public void setVarietySetsFromTextFiles() {

        InputStream redsFilePath = WineVarietyService.class.getResourceAsStream("/wine_catagories/reds.txt");
        InputStream whitesFilePath = WineVarietyService.class.getResourceAsStream("/wine_catagories/whites.txt");
        InputStream roseFilePath = WineVarietyService.class.getResourceAsStream("/wine_catagories/rose.txt");
        InputStream sparklingFilePath = WineVarietyService.class.getResourceAsStream("/wine_catagories/sparkling.txt");

        addWineToSet(redsFilePath, reds);
        addWineToSet(whitesFilePath, whites);
        addWineToSet(roseFilePath, rose);
        addWineToSet(sparklingFilePath, sparkling);

    }

    /**
     * This method takes the url of a wine varietys text file and puts all of them in a set.
     * @param url the filepath of the text file.
     * @param variety the set to put the lines of the text file in.
     */
    public void addWineToSet(InputStream url, HashSet<String> variety) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url));
            String varietyName;
            while ((varietyName = br.readLine()) != null) {
                variety.add(varietyName);
            }
        } catch (IOException e ) {
            e.printStackTrace();
        }

    }

    /**
     * This method takes in a grape variety and returns an int that relates to what colour wine the grape makes.
     * @param grape the grape variety
     * @return 0 if red, 1 if white, 2 if rose, 3 if sparkling, and -1 if it can't be found.
     */
    public int getVarietyFromGrape(String grape) {

        if (grape == null) {
            return -1;
        }
        if (reds.contains(grape)) {
            return 0;
        } else if (whites.contains(grape)) {
            return 1;
        } else if (rose.contains(grape)) {
            return 2;
        } else if (sparkling.contains(grape)) {
            return 3;
        } else {
            return -1;
        }

    }

}
