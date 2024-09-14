package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.repository.SearchDAO;
import seng202.team1.repository.WishlistDAO;

import java.text.Normalizer;
import java.util.ArrayList;

public class SearchWineService {
    private Wine currentWine;
    private ArrayList<Wine> wineList;

    private static SearchWineService instance;

    private static final Logger log = LogManager.getLogger(SearchWineService.class);

    /**
    Returns the instance and creates one if none exists.

    @return {@link SearchWineService instance}
     */
    public static SearchWineService getInstance()
    {
        if (instance == null) {
            instance = new SearchWineService();
        }
        return instance;
    }

    /**
     * Returns the current wine
     *
     * @return {@link Wine} currentWine
     */
    public Wine getCurrentWine() {
        return currentWine;
    }

    /**
     * Sets the current wine
     *
     * @param currentWine {@link Wine} currentWine
     */
    public void setCurrentWine(Wine currentWine) {
        this.currentWine = currentWine;
    }

    /**
     * Returns the stored wines list
     *
     * @return {@link ArrayList<Wine>} wines
     */
    public ArrayList<Wine> getWineList() {
        return wineList;
    }

    /**
     * to be done
     *
     * @param wineList {@link ArrayList<Wine>} wines
     */
    public void setWineList(ArrayList<Wine> wineList) {
        this.wineList = wineList;
    }

    public void searchWinesByTags(String tags)
    {
        tags = Normalizer.normalize(tags, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        String[] tagsArray = tags.split(",");

        ArrayList<String> tagList = new ArrayList<>();
        for (String tag : tagsArray) {
            tagList.add(tag.trim());
        }
        wineList = SearchDAO.getInstance().searchWineByTags(tagList);
    }

    /**
     * Fetches a list of all wines that contains the filterString within
     * the normalised name from the wines table in database and sets it to
     * the wineList variable.
     *
     * @param filterString A normalised {@link String} that contains what to search by
     */
    public void searchWinesByName(String filterString) {

        filterString = Normalizer.normalize(filterString, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();

        wineList = SearchDAO.getInstance().searchWineByName(filterString);
    }

    /**
     * Forwards the wineList from the DAO to the WishlistController
     *
     * @param userId is the id of the active user
     * @return wineList array of wines from the user's wishlist
     */
    public ArrayList<Wine> getWishlistWines(int userId) {
        return WishlistDAO.getInstance().fetchWines(userId);
    }
}
