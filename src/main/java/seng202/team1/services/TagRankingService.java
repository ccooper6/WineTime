package seng202.team1.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import seng202.team1.repository.DAOs.LogWineDao;

import java.util.HashMap;

/**
 * The service class responsible for using {@link LogWineDao} to get the required data for
 * {@link seng202.team1.gui.controllers.ProfileController}'s pie charts. Handles the checks if the user has enough
 * liked/disliked tags for the respective pie charts to be shown
 */
public class TagRankingService {
    private final LogWineDao logWineDao;

    /**
     * Initialises a TagRankingService with a LogWineDAO
     */
    public TagRankingService() {
        this.logWineDao = new LogWineDao();
    }

    /**
     * Checks if the user has 5 tags whose rating &lt; 0
     * @param uid user id
     * @return boolean, true if the user has 5 tags with negative ratings
     */
    public boolean hasEnoughDislikedTags(int uid) {
        return logWineDao.getMostDislikedTags(uid, 5).size() == 5;
    }

    /**
     * Checks if the user has 5 tags whose rating > 0
     * @param uid user id
     * @return boolean, true if the user has 5 tags with positive ratings
     */
    public boolean hasEnoughLikedTags(int uid) {
        return logWineDao.getFavouritedTags(uid, 5).size() == 5;
    }

    /**
     * Fetches the top numTags positively rated tags of the user and returns it to be used as data for pie charts
     * @param uid user id
     * @param numTags the limit on the number of data to return
     * @return an {@link ObservableList} of {@link PieChart.Data}
     */
    public ObservableList<PieChart.Data> getTopTagData(int uid, int numTags) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        HashMap<String, Integer> topFiveTags = logWineDao.getLikedTags(uid, numTags, true);
        for (String tagName : topFiveTags.keySet()) {
            data.add(new PieChart.Data(wrapText(tagName, 15), topFiveTags.get(tagName)));
        }
        return data;
    }

    /**
     * Fetches the top numTags negatively rated tags of the user and returns it to be used as data for pie charts
     * @param uid user id
     * @param numTags the limit on the number of data to return
     * @return an {@link ObservableList} of {@link PieChart.Data}
     */
    public ObservableList<PieChart.Data> getLowestTagData(int uid, int numTags) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        HashMap<String, Integer> topFiveTags = logWineDao.getMostDislikedTags(uid, numTags);
        for (String tagName : topFiveTags.keySet()) {
            data.add(new PieChart.Data(wrapText(tagName, 15), -1 * topFiveTags.get(tagName)));
        }
        return data;
    }

    /**
     * Manually wraps the given text to a specified length. Silly JavaFX won't do this for us 😢
     * @param text the text to wrap
     * @param length the length to wrap at
     * @return the wrapped text
     */
    private String wrapText(String text, int length) {
        StringBuilder wrappedText = new StringBuilder(text);
        int i = 0;
        while (i + length < wrappedText.length() && (i = wrappedText.lastIndexOf(" ", i + length)) != -1) {
            wrappedText.replace(i, i + 1, "\n");
        }
        return wrappedText.toString();
    }
}
