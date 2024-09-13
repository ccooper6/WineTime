package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.repository.SearchDAO;

import java.text.Normalizer;
import java.util.ArrayList;

public class SearchWineService {
    private Wine currentWine;
    private ArrayList<Wine> wineList;
    private String currentTags;

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
     * Returns the last used tags when searching by tags
     *
     * @return {@link String} last used tags
     */
    public String getCurrentTags()
    {
        return currentTags;
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
     * Searches the database for wines that matches all tags provided and sets
     * it to the wineList variable.
     *
     * @param tags A {@link String} of tags seperated by commas
     * @param limit The number of wines to select using {@link SearchDAO#UNLIMITED} for no limit
     */
    public void searchWinesByTags(String tags, int limit)
    {
        currentTags = tags;

        tags = Normalizer.normalize(tags, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        String[] tagsArray = tags.split(",");

        ArrayList<String> tagList = new ArrayList<>();
        for (String tag : tagsArray) {
            tagList.add(tag.trim());
        }
        wineList = SearchDAO.getInstance().searchWineByTags(tagList, limit);
    }

    /**
     * Fetches a list of wines up to a limit that contains the filterString within
     * the normalised name from the wines table in database and sets it to
     * the wineList variable.
     *
     * @param filterString A normalised {@link String} that contains what to search by
     * @param limit The number of wines to select using {@link SearchDAO#UNLIMITED} for no limit
     */
    public void searchWinesByName(String filterString, int limit) {

        filterString = Normalizer.normalize(filterString, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();

        wineList = SearchDAO.getInstance().searchWineByName(filterString, limit);
    }
}
