package seng202.team1.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.repository.DAOs.LogWineDao;

import java.util.HashMap;

/**
 * The service class responsible for using {@link LogWineDao} to get the required data for
 * {@link seng202.team1.gui.controllers.ProfileController}'s pie charts. Handles the checks if the user has enough
 * liked/disliked tags for the respective pie charts to be shown
 */
public class TagRankingService {
    private static final Logger LOG = LogManager.getLogger(TagRankingService.class);
    private final LogWineDao logWineDao;

    /**
     * initialises a logWineDao
     */
    public TagRankingService() {
        this.logWineDao = new LogWineDao();
    }

    /**
     * Checks if the user has 5 tags whose rating < 0
     * @param uid user id
     * @return true if the user has 5 tags with negative ratings
     */
    public boolean hasEnoughDislikedTags(int uid) {
        return logWineDao.getMostDislikedTags(uid, 5).size() == 5;
    }

    /**
     * Checks if the user has 5 tags whose rating > 0
     * @param uid user id
     * @return true if the user has 5 tags with positive ratings
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
            data.add(new PieChart.Data(tagName, topFiveTags.get(tagName)));
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
        HashMap<String, Integer> topFiveTags = logWineDao.getMostDislikedTags(uid, 5);
        for (String tagName : topFiveTags.keySet()) {
            data.add(new PieChart.Data(tagName, -1 * topFiveTags.get(tagName)));
        }
        return data;
    }
}
