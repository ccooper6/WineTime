package seng202.team1.services;

import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DAOs.WishlistDAO;

import java.text.Normalizer;
import java.util.ArrayList;

/**
 * Service class for searching wines in the database.
 * @author Yuhao Zhang, Caleb Cooper, Elise Newman, Isaac Macdonald
 */
public class SearchWineService {
    private Wine currentWine;
    private ArrayList<Wine> wineList;
    private String currentTags;
    private boolean sortDirection = true;
    private static SearchWineService instance;
    private String currentSearch;
    private String currentMethod;
    private boolean fromWishlist = false;

    private String currentCountryFilter = null;
    private String currentWineryFilter = null;
    private String currentVarietyFilter = null;

    private int currentMinYear = -1;
    private int currentMaxYear = -1;
    private int currentMinPoints = -1;
    private int currentMaxPoints = -1;
    private int currentMinPrice = -1;
    private int currentMaxPrice = -1;
    private ArrayList<String> selectedVarieties;

    /**
     * Returns the instance and creates one if none exists.
     *
     * @return {@link SearchWineService instance}
     */
    public static SearchWineService getInstance() {
        if (instance == null) {
            instance = new SearchWineService();
        }
        return instance;
    }

    /**
     * Returns the current wine.
     *
     * @return {@link Wine} currentWine
     */
    public Wine getCurrentWine() {
        return currentWine;
    }

    /**
     * Sets the current wine.
     *
     * @param currentWine {@link Wine} currentWine
     */
    public void setCurrentWine(Wine currentWine) {
        this.currentWine = currentWine;
    }

    /**
     * Returns the last used tags when searching by tags.
     *
     * @return {@link String} last used tags
     */
    public String getCurrentTags() {
        return currentTags;
    }

    /**
     * Returns the stored wines list.
     *
     * @return an array list of stored wines
     */
    public ArrayList<Wine> getWineList() {
        return wineList;
    }

    /**
     * Searches the database for wines that matches all tags provided and sets
     * it to the wineList variable.
     *
     * @param tags  A {@link String} of tags seperated by commas
     * @param limit The number of wines to select using {@link SearchDAO#UNLIMITED} for no limit
     */
    public void searchWinesByTags(String tags, int limit) {
        currentTags = tags;

        tags = Normalizer.normalize(tags, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        String[] tagsArray = tags.split(",");

        System.out.println(tagsArray.length);

        ArrayList<String> tagList = new ArrayList<>();
        for (String tag : tagsArray) {
            tagList.add(tag.trim());
        }
        wineList = SearchDAO.getInstance().searchWineByTags(tagList, limit);
        fromWishlist = false;
    }

    /**
     * Fetches a list of wines up to a limit that contains the filterString within
     * the normalised name from the wines table in database and sets it to
     * the wineList variable.
     *
     * @param filterString A normalised {@link String} that contains what to search by
     * @param limit        The number of wines to select using {@link SearchDAO#UNLIMITED} for no limit
     */
    public void searchWinesByName(String filterString, int limit) {

        filterString = Normalizer.normalize(filterString, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        filterString = filterString.trim();
        wineList = SearchDAO.getInstance().searchByNameAndFilter(new ArrayList<>(), 0, 101 , 0, 3000, filterString, limit);
        //wineList = SearchDAO.getInstance().searchWineByName(filterString, limit);
        fromWishlist = false;
    }

    /**
     * Sets wineList to an {@link ArrayList<Wine>} of recommended wines
     * @param limit an integer limit to the number of wines to recommend
     */
    public void searchWinesByRecommend(int limit) {
        wineList = RecommendWineService.getInstance().getRecommendedWines(User.getCurrentUser().getId(), limit);
    }

    /**
     * Sets the current search.
     *
     * @param currentSearch {@link String} currentSearch
     */
    public void setCurrentSearch(String currentSearch) {
        this.currentSearch = currentSearch;
    }

    /**
     * Returns the current search.
     *
     * @return {@link String} currentSearch
     */
    public String getCurrentSearch() {
        return currentSearch;
    }

    /**
     * Sets the current method.
     *
     * @param currentMethod {@link String} currentMethod
     */
    public void setCurrentMethod(String currentMethod) {
        this.currentMethod = currentMethod;
    }

    /**
     * Returns the current method.
     *
     * @return {@link String} currentMethod
     */
    public String getCurrentMethod() {
        return currentMethod;
    }

    /**
     * Finds all wines in the wishlist and saves them to the wineList for the WineDisplayController to get.
     * @param userId is the id of the active user
     */
    public void searchWinesByWishlist(int userId) {
        wineList = WishlistDAO.getInstance().fetchWines(userId);
        fromWishlist = true;
    }

    /**
     * If from wishlist, behavior in WineDisplay is different.
     * @return true if the prev page is wishlist, else false
     */
    public boolean getFromWishlist() {
        return fromWishlist;
    }
    public boolean getSortDirection(){ return sortDirection;}
    public void setSortDirection(boolean isUp) {
        sortDirection = isUp;
    }

    /**
     * Sets the current country filter
     * @param countryFilter the country filter
     */
    public void setCurrentCountryFilter(String countryFilter) {
        currentCountryFilter = countryFilter;
    }

    /**
     * Sets the current winery filter
     * @param wineryFilter the winery filter
     */
    public void setCurrentWineryFilter(String wineryFilter) {
        currentWineryFilter = wineryFilter;
    }

    /**
     * Sets the current variety filter
     * @param varietyFilter the country filter
     */
    public void setCurrentVarietyFilter(String varietyFilter) {
        currentVarietyFilter = varietyFilter;
    }

    /**
     * Gets the current country filter
     * @return the country filter
     */
    public String getCurrentCountryFilter() {
        return currentCountryFilter;
    }
    /**
     * Gets the current winery filter
     * @return the winery filter
     */
    public String getCurrentWineryFilter() {
        return currentWineryFilter;
    }
    /**
     * Gets the current variety filter
     * @return the variety filter
     */
    public String getCurrentVarietyFilter() {
        return currentVarietyFilter;
    }

}