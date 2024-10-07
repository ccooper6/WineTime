package seng202.team1.services;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Service class for the wine category feature.
 * @author Isaac Macdonald
 */
public class WineCategoryService {

    private int currentCategory = 0;
    private String currentCategoryTitle;
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
    public String getCurrentCategoryTitle()
    {
        return currentCategoryTitle;
    }

    /**
     * This method sets the current category title.
     * @param title the title to set
     */
    public void setCurrentCategoryTitle(String title) {
        title = title.replace(",", "");
        currentCategoryTitle = title;
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
