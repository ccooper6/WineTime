package seng202.team1.unittests.services;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.TagRankingService;

import java.util.ArrayList;

public class TagRankingServiceTest {
    static DatabaseManager databaseManager;
    static LogWineDao logWineDao;
    static TagRankingService tagRankingService;
    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException
    {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        tagRankingService = new TagRankingService();
        logWineDao = new LogWineDao();
        DatabaseManager.getInstance().forceReset();
    }
    @Test
    public void testNotEnoughDislikedTag() {
        logWineDao.likes(1, "2001", -1);
        Assertions.assertFalse(tagRankingService.hasEnoughDislikedTags(1));
    }

    @Test
    public void testHasEnoughDislikedTags() {
        //a rating of -1 is the bare minimum for a tag to be considered a disliked tag
        logWineDao.likes(1, "2001", -1);
        logWineDao.likes(1, "2002", -1);
        logWineDao.likes(1, "2003", -1);
        logWineDao.likes(1, "2004", -1);
        logWineDao.likes(1, "2005", -1);
        Assertions.assertTrue(tagRankingService.hasEnoughDislikedTags(1));
    }
    @Test
    public void testNotEnoughLikedTags() {
        logWineDao.likes(1, "2001", 1);
        Assertions.assertFalse(tagRankingService.hasEnoughLikedTags(1));
    }
    @Test
    public void testHasEnoughLikedTags() {
        //a rating of 1 is the bare minimum for a tag to be considered a liked tag
        logWineDao.likes(1, "2001", 1);
        logWineDao.likes(1, "2002", 1);
        logWineDao.likes(1, "2003", 1);
        logWineDao.likes(1, "2004", 1);
        logWineDao.likes(1, "2005", 1);
        Assertions.assertTrue(tagRankingService.hasEnoughLikedTags(1));
    }
    private boolean verifyTagData(ObservableList<PieChart.Data> tagData,boolean checkingTopTags, ArrayList<String> expectedTags) {
        for (PieChart.Data data : tagData) {
            if (!expectedTags.contains(data.getName())) {
                return false;
            }
        }
        return true;
    }
    @Test
    public void testGetTopTagData() {
        logWineDao.likes(1, "2000", 1);
        ArrayList<String> expectedTags = new ArrayList<>();
        String[] tagsToLike = new String[]{"2001", "2002", "2003", "2004", "2005"};
        for (String tag : tagsToLike) {
            logWineDao.likes(1, tag, 2);
            expectedTags.add(tag);
        }
        ObservableList<PieChart.Data> topTagData = tagRankingService.getTopTagData(1, 5);
        Assertions.assertTrue(verifyTagData(topTagData, true, expectedTags));
    }

    @Test
    public void testGetMostDislikedTagData() {
        logWineDao.likes(1, "2000", -1);
        ArrayList<String> expectedTags = new ArrayList<>();
        String[] tagsToDislike = new String[]{"2001", "2002", "2003", "2004", "2005"};
        for (String tag : tagsToDislike) {
            logWineDao.likes(1, tag, -2);
            expectedTags.add(tag);
        }
        ObservableList<PieChart.Data> topTagData = tagRankingService.getLowestTagData(1, 5);
        Assertions.assertTrue(verifyTagData(topTagData, false, expectedTags));
    }
}
