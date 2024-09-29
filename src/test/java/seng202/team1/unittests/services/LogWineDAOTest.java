package seng202.team1.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.DAOs.LogWineDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The unit test for the {@link LogWineDao} class
 *
 * @author Wen Sheng Thong
 */
public class LogWineDAOTest {
    static DatabaseManager databaseManager;
    static LogWineDao logWineDao;
    /**
     * Makes sure the database is set up before each test. Overwrites the prev test database with a clean test_database
     * before each test.
     * @throws InstanceAlreadyExistsException
     */
    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        logWineDao = new LogWineDao();
    }

    /**
     * Checks to see if likes are being added to the database accurately, also test the getLikes method
     */
    @Test
    public void testAddingLikes() {
        logWineDao.likes(1, "tag1", 5);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 10, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(5,result.get("tag1"));
    }

    /**
     * Test to ensure that tag values are different for different users
     */
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

    /**
     * Checks that {@link LogWineDao#updateLikesValue(int, String, int)} successfully updates the likes value
     */
    @Test
    public void testUpdateLikes() {
        logWineDao.likes(1, "tag1", 10);
        logWineDao.updateLikesValue(1, "tag1", -10);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 10, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(0,result.get("tag1"));
    }

    /**
     * Checks that {@link LogWineDao#likes(int, String, int)} calls {@link LogWineDao#updateLikesValue(int, String, int)}
     * properly
     */
    @Test
    public void testLikesCallsUpdateLikes() {
        logWineDao.likes(1, "tag1", 10);
        logWineDao.likes(1, "tag1", 10);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 10, true);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(20,result.get("tag1"));
    }

    /**
     * Checks that {@link LogWineDao#getLikedTags(int, int, boolean)} returns the top valued tags and that the maximum
     * tag returned is working
     */
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

    /**
     * Checks that {@link LogWineDao#getLikedTags(int, int, boolean)} does not break when user has no liked tags
     * and properly returns an empty hashmap.
     */
    @Test
    public void testNoTags() {
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, 2, true);
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * Checks that {@link LogWineDao#getLikedTags(int, boolean)} properly returns all the user's liked tags.
     */
    @Test
    public void testGetLikedNoLimit() {
        logWineDao.likes(1, "tag1", 10);
        logWineDao.likes(1, "tag2", 20);
        logWineDao.likes(1, "tag3", -100);
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, true);
        Assertions.assertTrue(result.size() == 3);
        Assertions.assertTrue(result.containsKey("tag1"));
        Assertions.assertEquals(10,result.get("tag1"));
        Assertions.assertTrue(result.containsKey("tag2"));
        Assertions.assertEquals(20,result.get("tag2"));
        Assertions.assertTrue(result.containsKey("tag3"));
        Assertions.assertEquals(-100,result.get("tag3"));
    }

    /**
     * Checks that {@link LogWineDao#reviews(int, int, int, String, String, ArrayList, boolean)} properly adds a review to the database as
     * well as tests that {@link LogWineDao#getUserReview(int, Boolean)} properly returns the user's reviews
     */
    @Test
    public void testAddingReview() {
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.reviews(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, false);
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(2,reviews.getFirst().getWid());
        Assertions.assertEquals(3,reviews.getFirst().getRating());
        Assertions.assertEquals("I like wine",reviews.getFirst().getReviewDescription());
        Assertions.assertEquals("2024-09-15 14:59:51",reviews.getFirst().getReviewDate());
    }
    /**
     * Checks that {@link LogWineDao#reviews(int, int, int, String, String, ArrayList, boolean)} properly adds a review to the database as
     * well as tests that {@link LogWineDao#getUserReview(int, Boolean)} properly returns the user's reviews correctly
     * based on the user id.
     */
    @Test
    public void testAddingGettingDiffReview() {
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.reviews(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, false);
        logWineDao.reviews(2,6,3,"I really like wine", "2025-09-15 14:59:51", likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, false);
        ArrayList<Review> reviews2 = logWineDao.getUserReview(2, false);
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(2,reviews.getFirst().getWid());
        Assertions.assertEquals(3,reviews.getFirst().getRating());
        Assertions.assertEquals("I like wine",reviews.getFirst().getReviewDescription());
        Assertions.assertEquals("2024-09-15 14:59:51",reviews.getFirst().getReviewDate());
        Assertions.assertEquals(2,reviews2.getFirst().getUid());
        Assertions.assertEquals(6,reviews2.getFirst().getWid());
        Assertions.assertEquals(3,reviews2.getFirst().getRating());
        Assertions.assertEquals("I really like wine",reviews2.getFirst().getReviewDescription());
        Assertions.assertEquals("2025-09-15 14:59:51",reviews2.getFirst().getReviewDate());
    }

    /**
     * Checks that {@link LogWineDao#getUserReview(int, int, Boolean)} returns up to only the specified number of reviews
     * and sorts it by most recent.
     */
    @Test
    public void testGettingOneRecentReview() {
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.reviews(1,3,-1,"I hate wine", "2023-09-15 14:59:51", likedTags, false);
        logWineDao.reviews(1,5,-1,"I hate 5 wine", "2022-09-15 14:59:51", likedTags, false);
        logWineDao.reviews(1,6,-1,"I hate 6 wine", "2022-09-15 14:59:51", likedTags, false);
        logWineDao.reviews(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1,1, true);
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(2,reviews.getFirst().getWid());
        Assertions.assertEquals(3,reviews.getFirst().getRating());
        Assertions.assertEquals("I like wine",reviews.getFirst().getReviewDescription());
        Assertions.assertEquals("2024-09-15 14:59:51",reviews.getFirst().getReviewDate());
        Assertions.assertEquals(1, reviews.size());
    }

    /**
     * Checks that {@link LogWineDao#reviews(int, int, int, String, String, ArrayList, boolean) calls
     * {@link LogWineDao#updateReview(int, int, int, String, String)}} correctly and updates the review appropriately
     * and that {@link LogWineDao#alreadyReviewExists(int, int)} returns the right boolean.
     */
    @Test
    public void testUpdatingReview() {
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.reviews(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, false);
        logWineDao.reviews(1,2,-10,"I no longer like wine", "2024-10-15 14:59:51", likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, false);
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(2,reviews.getFirst().getWid());
        Assertions.assertEquals(-10,reviews.getFirst().getRating());
        Assertions.assertEquals("I no longer like wine",reviews.getFirst().getReviewDescription());
        Assertions.assertEquals("2024-10-15 14:59:51",reviews.getFirst().getReviewDate());
        Assertions.assertEquals(1, reviews.size());
    }

    /**
     * Checks that {@link LogWineDao#getUserReview(int, int, Boolean)} returns a properly sorted array list of Reviews
     */
    @Test
    public void testOrderedReviews() {
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        logWineDao.reviews(1,4,10,"I can travel to future", "2026-10-15 14:59:51", likedTags, false);
        logWineDao.reviews(1,2,3,"I like wine", "2024-09-15 14:59:51", likedTags, false);
        logWineDao.reviews(1,3,-10,"I no longer like wine", "2025-10-15 14:59:51", likedTags, false);
        ArrayList<Review> reviews = logWineDao.getUserReview(1, true);
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(4,reviews.getFirst().getWid());
        Assertions.assertEquals(10,reviews.getFirst().getRating());
        Assertions.assertEquals("I can travel to future",reviews.getFirst().getReviewDescription());
        Assertions.assertEquals("2026-10-15 14:59:51",reviews.getFirst().getReviewDate());
        Assertions.assertEquals(1,reviews.get(1).getUid());
        Assertions.assertEquals(3,reviews.get(1).getWid());
        Assertions.assertEquals(-10,reviews.get(1).getRating());
        Assertions.assertEquals("I no longer like wine",reviews.get(1).getReviewDescription());
        Assertions.assertEquals("2025-10-15 14:59:51",reviews.get(1).getReviewDate());
        Assertions.assertEquals(1,reviews.get(2).getUid());
        Assertions.assertEquals(2,reviews.get(2).getWid());
        Assertions.assertEquals(3,reviews.get(2).getRating());
        Assertions.assertEquals("I like wine",reviews.get(2).getReviewDescription());
        Assertions.assertEquals("2024-09-15 14:59:51",reviews.get(2).getReviewDate());
    }

    /**
     * Checks that an empty ArrayList is returned when no reviews are found.
     */
    @Test
    public void testNoReviews() {
        ArrayList<Review> reviews = logWineDao.getUserReview(1, true);
        Assertions.assertTrue(reviews.isEmpty());
    }

    /**
     * Makes sure that only the top positively liked tags are returned by {@link LogWineDao#getFavouritedTags(int, int)}
     */
    @Test
    public void testGetFavouritedTags() {
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

    /**
     * makes sure that {@link LogWineDao#getDislikedTags(int)} only returns negatively rated tags
     */
    @Test
    public void testGetDislikedTest() {
        logWineDao.likes(1, "2006", 1000);
        logWineDao.likes(1, "2004", -1000);
        logWineDao.likes(2, "2006", 2000);
        ArrayList<String> hatedTags = logWineDao.getDislikedTags(1);
        Assertions.assertTrue(hatedTags.contains("2004"));
        Assertions.assertFalse(hatedTags.contains("2006"));
    }

    /**
     * make sure that {@link LogWineDao#getReviewedWines(int)} only returns wine id that the user has reviewed before.
     */
    @Test
    public void testGetReviewedWines() {
        logWineDao.reviews(1,4,10,"I can travel to future", "2026-10-15 14:59:51");
        logWineDao.reviews(1,2,3,"I like wine", "2024-09-15 14:59:51");
        logWineDao.reviews(2,3,-10,"I no longer like wine", "2025-10-15 14:59:51");
        ArrayList<Integer> reviewedWines = logWineDao.getReviewedWines(1);
        Assertions.assertTrue(reviewedWines.contains(4) && reviewedWines.contains(2));
        Assertions.assertFalse(reviewedWines.contains(3));
    }

}
