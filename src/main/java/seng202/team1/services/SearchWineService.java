package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DAOs.TagDAO;
import seng202.team1.repository.DAOs.WishlistDAO;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for searching wines in the database.
 */
public class SearchWineService {
    private Wine currentWine;
    private ArrayList<Wine> wineList;
    private boolean sortDirection = true;
    private static SearchWineService instance;
    private String currentSearch;
    private String currentMethod;

    private String currentCountryFilter = null;
    private String currentWineryFilter = null;
    private String currentVarietyFilter = null;

    private int currentMinYear = TagDAO.getInstance().getMinVintage();
    private int currentMaxYear = TagDAO.getInstance().getMaxVintage();
    private int currentMinPoints = TagDAO.getInstance().getMinPoints();
    private int currentMaxPoints = TagDAO.getInstance().getMaxPoints();
    private int currentMinPrice = TagDAO.getInstance().getMinPrice();
    private int currentMaxPrice = TagDAO.getInstance().getMaxPrice();
    private String searchOrder = "wine_name";
    private String prevSearch;
    private String prevDropDown = "Name";

    private Logger LOG = LogManager.getLogger(SearchWineService.class);

    /**
     * Default constructor for SearchWineService.
     */
    public SearchWineService() {}

    /**
     * Returns the instance and creates one if none exists.
     * @return SearchWineService instance
     */
    public static SearchWineService getInstance() {
        if (instance == null) {
            instance = new SearchWineService();
        }
        return instance;
    }

    /**
     * resets the filters back to default values.
     */
    public void resetFilters() {
        currentCountryFilter = null;
        currentWineryFilter = null;
        currentVarietyFilter = null;
        currentMinYear = TagDAO.getInstance().getMinVintage();
        currentMaxYear = TagDAO.getInstance().getMaxVintage();
        currentMinPoints = TagDAO.getInstance().getMinPoints();
        currentMaxPoints = TagDAO.getInstance().getMaxPoints();
        currentMinPrice = TagDAO.getInstance().getMinPrice();
        currentMaxPrice = TagDAO.getInstance().getMaxPrice();
    }

    /**
     * Returns the current wine.
     * @return Wine currentWine
     */
    public Wine getCurrentWine() {
        return currentWine;
    }

    /**
     * Sets the current wine.
     * @param currentWine Wine currentWine
     */
    public void setCurrentWine(Wine currentWine) {
        this.currentWine = currentWine;
    }

    /**
     * Returns the stored wines list.
     * @return ArrayList&lt;Wine&lt; of stored wines
     */
    public ArrayList<Wine> getWineList() {
        return wineList;
    }

    /**
     * Searches the database for wines that matches all tags provided and sets
     * it to the wineList variable.
     * @param tags  A String of tags seperated by commas
     * @param limit The number of wines to select using -1 for no limit
     */
    public void searchWinesByTags(String tags, int limit) {
        tags = Normalizer.normalize(tags, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();

        ArrayList<String> tagsList = getTagsFromString(tags);

        if (limit == -1) {
            limit = SearchDAO.UNLIMITED;
        }

        wineList = SearchDAO.getInstance().searchWineByTags(tagsList, limit);
    }

    /**
     * Creates an ArrayList of the given tags.
     *
     * @param tags The string of the tags to convert separated by commas
     * @return An ArrayList of the tags given. If the given string tags is null, an empty ArrayList will be returned
     */
    public ArrayList<String> getTagsFromString(String tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }

        String[] tagsArray = tags.split(",");

        ArrayList<String> tagList = new ArrayList<>();
        for (String tag : tagsArray) {
            tagList.add(tag.trim());
        }

        return tagList;
    }

    /**
     * Fetches a list of wines up to a limit that contains the filterString within
     * the normalised name from the wines table in database and sets it to
     * the wineList variable.
     *
     * @param filterString A normalised String that contains what to search by
     */
    public void searchWinesByName(String filterString) {
        if (filterString == null) {
            // dont perform the search
            LOG.error("Error: Could not get search query string.");
            return;
        }

        filterString = Normalizer.normalize(filterString, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        filterString = filterString.trim();
        if (System.getProperty("test.env") == null) {
            if (!FXWrapper.getInstance().getCurrentPage().equals("searchWine")) {
                resetFilters();
            }
        }

        searchWineByNameAndFilter(filterString, getFilterStrings(), currentMinPoints, currentMaxPoints, currentMinYear, currentMaxYear, currentMinPrice, currentMaxPrice, searchOrder);
        prevSearch = filterString;
    }

    /**
     * Calls SearchDAO to search for wines by name and applying current filters.
     *
     * @param filterString The name string to match
     * @param tags An ArrayList of Strings containing tags for
     * @param minPoints The minimum points the searched wines must have
     * @param maxPoints The maximum points the searched wines must have
     * @param minYear The minimum vintage the searched wines must be
     * @param maxYear The maximum vintage the searched wines must be
     * @param minPrice The minimum price the searched wines must be
     * @param maxPrice The maximum price the searched wines must be
     * @param searchOrder The attribute of the wine to search by. Must be "points", "vintage", "price", "wine_name" or null to indicate no sorting
     */
    public void searchWineByNameAndFilter(String filterString, ArrayList<String> tags, int minPoints, int maxPoints, int minYear, int maxYear, int minPrice, int maxPrice, String searchOrder) {
        if (filterString == null) {
            LOG.error("Error: Could not get search query string.");
            return;
        } else if (tags == null) {
            LOG.error("Error: Could not get search query tags.");
            return;
        } else if (searchOrder != null && !(new ArrayList<>(List.of("points", "vintage", "price", "wine_name"))).contains(searchOrder.toLowerCase())) {
            LOG.error("Error: Invalid search order.");
            return;
        }

        searchOrder = searchOrder.toLowerCase();

        wineList = SearchDAO.getInstance().searchWineByTagsAndFilter(tags, minPoints, maxPoints, minYear, maxYear, minPrice, maxPrice, filterString, searchOrder);
    }

    /**
     * Gets the non-null filter strings.
     * @return ArrayList of filter strings
     */
    private ArrayList<String> getFilterStrings() {
        ArrayList<String> results = new ArrayList<>();
        if (currentCountryFilter != null) {
            String normCountryFilter = Normalizer.normalize(currentCountryFilter, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
            normCountryFilter = normCountryFilter.trim();
            results.add(normCountryFilter);
        }
        if (currentWineryFilter != null) {
            String normWineryFilter = Normalizer.normalize(currentWineryFilter, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
            normWineryFilter = normWineryFilter.trim();
            results.add(normWineryFilter);
        }
        if (currentVarietyFilter != null) {
            String normVarietyFilter = Normalizer.normalize(currentVarietyFilter, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
            normVarietyFilter = normVarietyFilter.trim();
            results.add(normVarietyFilter);
        }
        return results;
    }

    /**
     * Sets wineList to an ArrayList&lt;Wine&gt; of recommended wines.
     * @param limit an integer limit to the number of wines to recommend
     * @param userID the current users id
     */
    public void searchWinesByRecommend(int userID, int limit) {
        currentMethod = "recommended";
        wineList = RecommendWineService.getInstance().getRecommendedWines(userID, limit);
    }

    /**
     * Sets the current search.
     * @param currentSearch String currentSearch
     */
    public void setCurrentSearch(String currentSearch) {
        this.currentSearch = currentSearch;
    }

    /**
     * Returns the current search.
     * @return String currentSearch
     */
    public String getCurrentSearch() {
        return currentSearch;
    }

    /**
     * Sets the current method.
     * @param currentMethod String currentMethod
     */
    public void setCurrentMethod(String currentMethod) {
        this.currentMethod = currentMethod;
    }

    /**
     * Returns the current method.
     * @return String currentMethod
     */
    public String getCurrentMethod() {
        return currentMethod;
    }

    /**
     * Finds all wines in the wishlist and saves them to the wineList for the WineDisplayController to get.
     * @param userId is the id of the current user
     */
    public void searchWinesByWishlist(int userId) {
        wineList = WishlistDAO.getInstance().fetchWines(userId);
    }

    /**
     * Direction of the sort arrow as a boolean value.
     * @return boolean, true = up, false = down
     */
    public boolean getSortDirection() {
        return sortDirection;
    }

    /**
     * Sets the direction to up depending on the bool.
     * @param isUp boolean, true to set it to up
     */
    public void setSortDirection(boolean isUp) {
        sortDirection = isUp;
    }

    /**
     * Sets the current country filter.
     * @param countryFilter the country filter
     */
    public void setCurrentCountryFilter(String countryFilter) {
        currentCountryFilter = countryFilter;
    }

    /**
     * Sets the current winery filter.
     * @param wineryFilter the winery filter
     */
    public void setCurrentWineryFilter(String wineryFilter) {
        currentWineryFilter = wineryFilter;
    }

    /**
     * Sets the current variety filter.
     * @param varietyFilter the variety filter
     */
    public void setCurrentVarietyFilter(String varietyFilter) {
        currentVarietyFilter = varietyFilter;
    }

    /**
     * Gets the current country filter.
     * @return the country filter
     */
    public String getCurrentCountryFilter() {
        return currentCountryFilter;
    }

    /**
     * Gets the current winery filter.
     * @return the winery filter
     */
    public String getCurrentWineryFilter() {
        return currentWineryFilter;
    }

    /**
     * Gets the current variety filter.
     * @return the variety filter
     */
    public String getCurrentVarietyFilter() {
        return currentVarietyFilter;
    }

    /**
     * Sets the current minimum year.
     * @param minYear the minimum year
     */
    public void setCurrentMinYear(int minYear) {
        currentMinYear = minYear;
    }

    /**
     * Sets the current maximum year.
     * @param maxYear the maximum year
     */
    public void setCurrentMaxYear(int maxYear) {
        currentMaxYear = maxYear;
    }

    /**
     * Sets the current minimum points.
     * @param minPoints the minimum points
     */
    public void setCurrentMinPoints(int minPoints) {
        currentMinPoints = minPoints;
    }

    /**
     * Sets the current max points.
     * @param maxPoints the max points
     */
    public void setCurrentMaxPoints(int maxPoints) {
        currentMaxPoints = maxPoints;
    }

    /**
     * Sets the current minimum price.
     * @param minPrice the minimum price
     */
    public void setCurrentMinPrice(int minPrice) {
        currentMinPrice = minPrice;
    }

    /**
     * Sets the current max price.
     * @param maxPrice the max price
     */
    public void setCurrentMaxPrice(int maxPrice) {

        if (maxPrice < 200) {
            currentMaxPrice = maxPrice;
        } else {
            currentMaxPrice = 3300;
        }
    }

    /**
     * Returns the current min year.
     * @return the min year
     */
    public int getCurrentMinYear() {
        return currentMinYear;
    }

    /**
     * Returns the current max year.
     * @return the max year
     */
    public int getCurrentMaxYear() {
        return currentMaxYear;
    }

    /**
     * Returns the current min points.
     * @return the min points
     */
    public int getCurrentMinPoints() {
        return currentMinPoints;
    }

    /**
     * Returns the current max points.
     * @return the max points
     */
    public int getCurrentMaxPoints() {
        return currentMaxPoints;
    }

    /**
     * Returns the current min price.
     * @return the min price
     */
    public int getCurrentMinPrice() {
        return currentMinPrice;
    }

    /**
     * Returns the current max price.
     * @return the max price
     */
    public int getCurrentMaxPrice() {
        return currentMaxPrice;
    }

    /**
     * Sets search order var and re-queries using previous query.
     * Triggered by sort-by dropdown in search page
     * @param searchOrder is the ORDER BY param which is the column name
     */
    public void setSearchOrder(String searchOrder) {
        this.searchOrder = searchOrder;
        searchWinesByName(prevSearch);
    }

    /**
     * Stores the dropdown sort by for later.
     * @param prevDropDown is the name of the dropdown title: "Name" etc.
     */
    public void setDropDown(String prevDropDown) {
        this.prevDropDown = prevDropDown;
    }

    /**
     * Gets the previous dropdown.
     * @return previous dropdown
     */
    public String getPrevDropDown() {
        return prevDropDown;
    }
}