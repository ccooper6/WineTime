package seng202.team1.services;

import seng202.team1.gui.FXWrapper;
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

    private int currentMinYear = 1821;
    private int currentMaxYear = 2017;
    private int currentMinPoints = 80;
    private int currentMaxPoints = 100;
    private int currentMinPrice = 4;
    private int currentMaxPrice = 3300;
    private ArrayList<String> selectedVarieties;
    private String searchOrder = "wine_name";
    private String prevSearch;
    private String prevDropDown = "Name";

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
     * resets the filters back to default values.
     */
    public void resetFilters() {
        currentCountryFilter = null;
        currentWineryFilter = null;
        currentVarietyFilter = null;
        currentMinYear = 1821;
        currentMaxYear = 2017;
        currentMinPoints = 80;
        currentMaxPoints = 100;
        currentMinPrice = 4;
        currentMaxPrice = 3300;
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
        System.out.println(System.getProperty("test.env"));
        if (System.getProperty("test.env") == null) {
            if (FXWrapper.getInstance().getCurrentPage().equals("searchWine")) {
                System.out.println("from search page");
            } else {
                resetFilters();
                System.out.println("RESET FILTERS");
            }
        }
        wineList = SearchDAO.getInstance().searchWineByTagsAndFilter(getFilterStrings(), currentMinPoints, currentMaxPoints , currentMinYear, currentMaxYear, filterString);
        prevSearch = filterString;
        fromWishlist = false;
    }

    /**
     * Gets the non-null filter strings
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
     * Direction of the sort arrow as a boolean value
     * @return true = up, false = down
     */
    public boolean getSortDirection() {
        return sortDirection;
    }

    /**
     * Sets the direction to up depending on the bool
     * @param isUp boolean, true to set it to up
     */
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

    /**
     * Sets the current minimum year
     * @param minYear the minimum year
     */
    public void setCurrentMinYear(int minYear) {
        currentMinYear = minYear;
    }

    /**
     * Sets the current minimum year
     * @param maxYear the minimum year
     */
    public void setCurrentMaxYear(int maxYear) {
        currentMaxYear = maxYear;
    }

    /**
     * Sets the current minimum year
     * @param minPoints the minimum year
     */
    public void setCurrentMinPoints(int minPoints) {
        currentMinPoints = minPoints;
    }

    /**
     * Sets the current max points
     * @param maxPoints the max points
     */
    public void setCurrentMaxPoints(int maxPoints) {
        currentMaxPoints = maxPoints;
    }

    /**
     * Sets the current minimum price
     * @param minPrice the minimum price
     */
    public void setCurrentMinPrice(int minPrice) {
        currentMinPrice = minPrice;
    }

    /**
     * Sets the current max price
     * @param maxPrice the max price
     */
    public void setCurrentMaxPrice(int maxPrice) {
        currentMaxPrice = maxPrice;
    }

    /**
     * Returns the current min year
     * @return the min year
     */
    public int getCurrentMinYear() {
        return currentMinYear;
    }

    /**
     * Returns the current max year
     * @return the max year
     */
    public int getCurrentMaxYear() {
        return currentMaxYear;
    }

    /**
     * Returns the current min points
     * @return the min points
     */
    public int getCurrentMinPoints() {
        return currentMinPoints;
    }

    /**
     * Returns the current max points
     * @return the max points
     */
    public int getCurrentMaxPoints() {
        return currentMaxPoints;
    }

    /**
     * Returns the current min price
     * @return the min price
     */
    public int getCurrentMinPrice() {
        return currentMinPrice;
    }

    /**
     * Returns the current max price
     * @return the max price
     */
    public int getCurrentMaxPrice() {
        return currentMaxPrice;
    }

    /**
     *  Sets search order var and requeries using previous query
     * Triggered by sort-by dropdown in search page
     * @param searchOrder is the ORDER BY param which is the column name
     */
    public void setSearchOrder(String searchOrder) {
        System.out.println("search order: " + searchOrder);
        this.searchOrder = searchOrder;
        searchWinesByName(prevSearch, SearchDAO.UNLIMITED);
    }
    /**
     * Stores the dropdown sort by for later
     * @param prevDropDown is the name of the dropdown title: "Name" etc.
     */
    public void setDropDown(String prevDropDown) {
        this.prevDropDown = prevDropDown;
    }
    public String getPrevDropDown() {
        return prevDropDown;
    }
}