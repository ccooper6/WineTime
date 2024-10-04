package seng202.team1.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.repository.DAOs.LogWineDao;

import java.util.HashMap;

public class TagRankingService {
    private static final Logger LOG = LogManager.getLogger(TagRankingService.class);
    private LogWineDao logWineDao;

    public TagRankingService() {
        this.logWineDao = new LogWineDao();
    }
    public boolean hasEnoughDislikedTags(int uid) {
        return logWineDao.getMostDislikedTags(uid, 5).size() == 5;
    }
    public boolean hasEnoughLikedTags(int uid) {
        return logWineDao.getFavouritedTags(uid, 5).size() == 5;
    }
    public ObservableList<PieChart.Data> getTopTagData(int uid, int numTags) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        HashMap<String, Integer> topFiveTags = logWineDao.getLikedTags(uid, numTags, true);
        for (String tagName : topFiveTags.keySet()) {
            data.add(new PieChart.Data(tagName, topFiveTags.get(tagName)));
        }
        return data;
    }

    public ObservableList<PieChart.Data> getLowestTagData(int uid, int numTags) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        HashMap<String, Integer> topFiveTags = logWineDao.getMostDislikedTags(uid, 5);
        for (String tagName : topFiveTags.keySet()) {
            data.add(new PieChart.Data(tagName, topFiveTags.get(tagName)));
        }
        return data;
    }
}
