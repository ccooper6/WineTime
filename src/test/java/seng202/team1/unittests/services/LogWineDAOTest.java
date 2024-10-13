package seng202.team1.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDAO;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LogWineDAOTest {
    static LogWineDAO logWineDao;

    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.removeInstance();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        logWineDao = new LogWineDAO();
    }

    @Test
    public void testAddingLikes() {
        logWineDao.likes(1, "tag1", 5);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 10, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(5,result.get("tag1"));
    }

    @Test
    public void testAddingDiffUserLikes() {
        logWineDao.likes(1, "tag1", 5);
        logWineDao.likes(2, "tag1", -4);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 10, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(5,result.get("tag1"));
        HashMap<String, Integer> result2 = logWineDao.getLikedTags(2, 10, true);
        Assertions.assertTrue(result2.containsKey("tag1"));
        Assertions.assertEquals(-4,result2.get("tag1"));
    }

    @Test
    public void testUpdateLikes() {
        logWineDao.likes(1, "tag1", 10);
        logWineDao.updateLikesValue(1, "tag1", -10);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 10, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(0,result.get("tag1"));
    }

    @Test
    public void testLikesCallsUpdateLikes() {
        logWineDao.likes(1, "tag1", 10);
        logWineDao.likes(1, "tag1", 10);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 10, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(20,result.get("tag1"));
    }

    @Test
    public void testGetLikedTags() {
        logWineDao.likes(1, "tag1", 10);
        logWineDao.likes(1, "tag2", 20);
        logWineDao.likes(1, "tag3", -100);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 2, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(10,result.get("tag1"));
        Assertions.assertTrue(result.containsKey("tag2"));
        Assertions.assertEquals(20,result.get("tag2"));
        Assertions.assertFalse(result.containsKey("tag3"));
    }

    @Test
    public void testNoTags() {
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 2, true);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetLikedNoLimit() {
        logWineDao.likes(1, "tag1", 10);
        logWineDao.likes(1, "tag2", 20);
        logWineDao.likes(1, "tag3", -100);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, SearchDAO.UNLIMITED, true);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(10, result.get("tag1"));
        Assertions.assertTrue(result.containsKey("tag2"));
        Assertions.assertEquals(20, result.get("tag2"));
        Assertions.assertTrue(result.containsKey("tag3"));
        Assertions.assertEquals(-100, result.get("tag3"));
    }

    @Test
    public void testAddingReview() {
        ArrayList<String> likedTags = new ArrayList<>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.doReview(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, SearchDAO.UNLIMITED, false);
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(2,reviews.getFirst().getWid());
        Assertions.assertEquals(3,reviews.getFirst().getRating());
        Assertions.assertEquals("I like wine",reviews.getFirst().getDescription());
        Assertions.assertEquals("2024-09-15 14:59:51",reviews.getFirst().getDate());
    }

    @Test
    public void testAddingGettingDiffReview() {
        ArrayList<String> likedTags = new ArrayList<>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.doReview(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, likedTags, false);
        logWineDao.doReview(2,6,3,"I really like wine", "2025-09-15 14:59:51", likedTags, likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, SearchDAO.UNLIMITED, false);
        ArrayList<Review> reviews2 = logWineDao.getUserReview(2, SearchDAO.UNLIMITED, false);
        Assertions.assertEquals(1, reviews.getFirst().getUid());
        Assertions.assertEquals(2, reviews.getFirst().getWid());
        Assertions.assertEquals(3, reviews.getFirst().getRating());
        Assertions.assertEquals("I like wine", reviews.getFirst().getDescription());
        Assertions.assertEquals("2024-09-15 14:59:51", reviews.getFirst().getDate());
        Assertions.assertEquals(2, reviews2.getFirst().getUid());
        Assertions.assertEquals(6, reviews2.getFirst().getWid());
        Assertions.assertEquals(3, reviews2.getFirst().getRating());
        Assertions.assertEquals("I really like wine", reviews2.getFirst().getDescription());
        Assertions.assertEquals("2025-09-15 14:59:51", reviews2.getFirst().getDate());
    }

    @Test
    public void testGettingOneRecentReview() {
        ArrayList<String> likedTags = new ArrayList<>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.doReview(1,3,-1,"I hate wine", "2023-09-15 14:59:51", likedTags, likedTags, false);
        logWineDao.doReview(1,5,-1,"I hate 5 wine", "2022-09-15 14:59:51", likedTags, likedTags, false);
        logWineDao.doReview(1,6,-1,"I hate 6 wine", "2022-09-15 14:59:51", likedTags, likedTags, false);
        logWineDao.doReview(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1,1, true);
        Assertions.assertEquals(1, reviews.getFirst().getUid());
        Assertions.assertEquals(2, reviews.getFirst().getWid());
        Assertions.assertEquals(3, reviews.getFirst().getRating());
        Assertions.assertEquals("I like wine", reviews.getFirst().getDescription());
        Assertions.assertEquals("2024-09-15 14:59:51", reviews.getFirst().getDate());
        Assertions.assertEquals(1, reviews.size());
    }

    @Test
    public void testUpdatingReview() {
        ArrayList<String> likedTags = new ArrayList<>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.doReview(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, likedTags, false);
        logWineDao.doReview(1,2,-10,"I no longer like wine", "2024-10-15 14:59:51", likedTags, likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, SearchDAO.UNLIMITED, false);
        Assertions.assertEquals(1, reviews.getFirst().getUid());
        Assertions.assertEquals(2, reviews.getFirst().getWid());
        Assertions.assertEquals(-10, reviews.getFirst().getRating());
        Assertions.assertEquals("I no longer like wine", reviews.getFirst().getDescription());
        Assertions.assertEquals("2024-10-15 14:59:51", reviews.getFirst().getDate());
        Assertions.assertEquals(1, reviews.size());
    }

    @Test
    public void testOrderedReviews() {
        ArrayList<String> likedTags = new ArrayList<>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.doReview(1,4,10,"I can travel to future", "2026-10-15 14:59:51", likedTags, likedTags, false);
        logWineDao.doReview(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, likedTags, false);
        logWineDao.doReview(1,3,-10,"I no longer like wine", "2025-10-15 14:59:51", likedTags, likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, SearchDAO.UNLIMITED, true);
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(4,reviews.getFirst().getWid());
        Assertions.assertEquals(10,reviews.getFirst().getRating());
        Assertions.assertEquals("I can travel to future",reviews.getFirst().getDescription());
        Assertions.assertEquals("2026-10-15 14:59:51",reviews.getFirst().getDate());
        Assertions.assertEquals(1,reviews.get(1).getUid());
        Assertions.assertEquals(3,reviews.get(1).getWid());
        Assertions.assertEquals(-10,reviews.get(1).getRating());
        Assertions.assertEquals("I no longer like wine",reviews.get(1).getDescription());
        Assertions.assertEquals("2025-10-15 14:59:51",reviews.get(1).getDate());
        Assertions.assertEquals(1,reviews.get(2).getUid());
        Assertions.assertEquals(2,reviews.get(2).getWid());
        Assertions.assertEquals(3,reviews.get(2).getRating());
        Assertions.assertEquals("I like wine",reviews.get(2).getDescription());
        Assertions.assertEquals("2024-09-15 14:59:51",reviews.get(2).getDate());
    }

    @Test
    public void testNoReviews() {
        ArrayList<Review> reviews = logWineDao.getUserReview(1, SearchDAO.UNLIMITED, true);
        Assertions.assertTrue(reviews.isEmpty());
    }

    @Test
    public void testGetFavoritedTags() {
        String[] tags = {"2006", "2005", "2004"};
        for (String tag : tags) {
            logWineDao.likes(1,tag, 100);
        }
        logWineDao.likes(1, "2006", -1000);
        logWineDao.likes(1, "2004", -1000);
        logWineDao.likes(2, "2006", 1000);
        ArrayList<String> favTags = logWineDao.getFavouritedTags(1,2);
        Assertions.assertTrue(favTags.contains("2005"));
        Assertions.assertFalse(favTags.contains("2006"));
    }

    @Test
    public void testGetDislikedTest() {
        logWineDao.likes(1, "2006", 1000);
        logWineDao.likes(1, "2004", -1000);
        logWineDao.likes(2, "2006", 2000);
        ArrayList<String> hatedTags = logWineDao.getDislikedTags(1);
        Assertions.assertTrue(hatedTags.contains("2004"));
        Assertions.assertFalse(hatedTags.contains("2006"));
    }

    @Test
    public void testGetReviewedWines() {
        ArrayList<String> likedTags = new ArrayList<>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.doReview(1,4,10,"I can travel to future", "2026-10-15 14:59:51", null, likedTags,true);
        logWineDao.doReview(1,2,3,"I like wine", "2024-09-15 14:59:51", null, likedTags, true);
        logWineDao.doReview(2,3,-10,"I no longer like wine", "2025-10-15 14:59:51", null, likedTags, true);
        ArrayList<Integer> reviewedWines = logWineDao.getReviewedWines(1);
        Assertions.assertTrue(reviewedWines.contains(4) && reviewedWines.contains(2));
        Assertions.assertFalse(reviewedWines.contains(3));
    }

}
