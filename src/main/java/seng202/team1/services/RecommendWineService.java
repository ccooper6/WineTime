package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.SearchDAO;

import java.util.ArrayList;

/**
 * The service class which handles getting wine recommendations from the database
 * as well as containing methods that handles the logic on whether to recommend wines
 * based on the amount of data we have on the user.
 * @author Wen Sheng Thong
 */
public class RecommendWineService {
    private static final Logger LOG = LogManager.getLogger(RecommendWineService.class);
    private static RecommendWineService instance;
    private SearchDAO searchDAO;
    private LogWineDao logWineDao;

    /**
     * Returns the singleton instance of the RecommendWineService.
     * @return the instance of the RecommendWineService
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
    public RecommendWineService () {
        this.logWineDao = new LogWineDao();
        this.searchDAO = new SearchDAO();
    }

    /**
     * Returns a {@link Boolean} on whether the user has liked enough tags to start recommending wines to them.
     * @param uid the user id
     * @return a {@link Boolean}. True if the user has positively liked 3 tags.
     */
    public Boolean hasEnoughFavouritesTag(int uid) {
        ArrayList<String> likedTags = logWineDao.getFavouritedTags(uid, 3);
        LOG.info("User has liked {} tags", likedTags.size());
        return likedTags.size() == 3;
    }

    /**
     * Returns an {@link ArrayList<Wine>} of wines that are recommended to the user. If no wines are recommended
     * then it will ignore the user's disliked tags and resend another recommendation request.
     * @param uid the user id
     * @param limit the limit on the number of wines to recommend
     * @return an {@link ArrayList<Wine>} of {@link Wine}
     */
    public ArrayList<Wine> getRecommendedWines(int uid, int limit) {
        ArrayList<Integer> winesToAvoid = logWineDao.getReviewedWines(uid);
        ArrayList<Wine> recommendedWines;
        ArrayList<String> likedTags = logWineDao.getFavouritedTags(uid, 5);
        ArrayList<String> dislikedTags = logWineDao.getDislikedTags(uid);
        recommendedWines = searchDAO.reccWineByTags(likedTags,dislikedTags,winesToAvoid,limit);
        if (recommendedWines.isEmpty()) {
            dislikedTags = new ArrayList<>();
            LOG.info("Not enough wines to recommend, retrying without disliked tags");
            recommendedWines = searchDAO.reccWineByTags(likedTags,dislikedTags,winesToAvoid,limit);
        } else {
            LOG.info("We have enough wines to recommend, displaying {} wines!", recommendedWines.size());
        }
        return recommendedWines;
    }


}
