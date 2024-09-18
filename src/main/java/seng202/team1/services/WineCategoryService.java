package seng202.team1.services;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Service class for the wine category feature.
 * @author Isaac Macdonald
 */
public class WineCategoryService {

    private int currentCategory = 0;
    private final ArrayList<String> categoryTitles = new ArrayList<>(Arrays.asList("Bordeaux Merlot", "Marlborough Sauvignon Blanc", "Tuscany Sangiovese", "Hawke's Bay Syrah",
                                                                     "Rioja Tempranillo", "Mendoza Malbec", "Napa Valley Cabernet Sauvignon", "Central Otago Pinot Noir"));
    private static WineCategoryService instance;

    /**
     Returns the instance and creates one if none exists.

     @return {@link WineCategoryService instance}
     */
    public static WineCategoryService getInstance()
    {
        if (instance == null) {
            instance = new WineCategoryService();
        }
        return instance;
    }

    /**
     * This method gets the current category to set the title.
     * @return the current category
     */
    public int getCurrentCategory()
    {
        return currentCategory;
    }

    /**
     * Gets the category titles to set the current category title.
     * @return the category titles arraylist.
     */
    public ArrayList<String> getCategoryTitles()
    {
        return categoryTitles;
    }

    /**
     * This method increments current category.
     */
    public void incrementCurrentCategory()
    {
        currentCategory++;
    }

    /**
     * This method resets the current category to 0.
     */
    public void resetCurrentCategory()
    {
        currentCategory = 0;
    }
}
