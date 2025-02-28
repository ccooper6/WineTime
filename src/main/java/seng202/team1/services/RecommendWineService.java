package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDAO;
import seng202.team1.repository.DAOs.SearchDAO;

import java.util.ArrayList;

/**
 * The service class which handles getting wine recommendations from the database.
 * as well as containing methods that handles the logic on whether to recommend wines
 * based on the amount of data we have on the user
 */
public class RecommendWineService {
    private static final Logger LOG = LogManager.getLogger(RecommendWineService.class);
    private static RecommendWineService instance;
    private final SearchDAO searchDAO;
    private final LogWineDAO logWineDAO;

    /**
     * Returns an instance of the RecommendWineService.
     * @return RecommendWineService
     */
    public static RecommendWineService getInstance() {
        if (instance == null) {
            instance = new RecommendWineService();
        }
        return instance;
    }

    /**
     * Constructor for RecommendWineService.
     */
    public RecommendWineService() {
        this.logWineDAO = new LogWineDAO();
        this.searchDAO = new SearchDAO();
    }

    /**
     * Returns a Boolean on whether the user has liked enough tags to start recommending wines to them.
     * @param uid the user id
     * @return a Boolean. True if the user has positively liked 5 tags.
     */
    public boolean hasEnoughFavouritesTag(int uid) {
        ArrayList<String> likedTags = logWineDAO.getFavouritedTags(uid, 5);
        return likedTags.size() == 5;
    }

    /**
     * Returns an ArrayList&lt;Wine&gt; of wines that are recommended to the user. If no wines are recommended
     * then it will ignore the user's disliked tags and resend another recommendation request.
     * @param uid the user id
     * @param limit the limit on the number of wines to recommend
     * @return an ArrayList &lt;Wine&gt; of Wine
     */
    public ArrayList<Wine> getRecommendedWines(int uid, int limit) {
        ArrayList<Integer> winesToAvoid = logWineDAO.getReviewedWines(uid);
        ArrayList<Wine> recommendedWines;
        ArrayList<String> likedTags = logWineDAO.getFavouritedTags(uid, 5);
        ArrayList<String> dislikedTags = logWineDAO.getDislikedTags(uid);
        recommendedWines = searchDAO.getRecommendedWines(likedTags,dislikedTags,winesToAvoid,limit);
        if (recommendedWines.isEmpty()) {
            dislikedTags = new ArrayList<>();
            LOG.info("NOT ENOUGH WINES IN RECOMMENDED");
            recommendedWines = searchDAO.getRecommendedWines(likedTags,dislikedTags,winesToAvoid,limit);
        } else {
            LOG.info("WE HAVE ENOUGH WINES TO RECOMMEND, WE HAVE {} WINES", recommendedWines.size());
        }
        return recommendedWines;
    }


}
