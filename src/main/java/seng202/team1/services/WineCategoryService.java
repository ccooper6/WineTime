package seng202.team1.services;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Service class for the wine category feature.
 */
public class WineCategoryService {
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
}
