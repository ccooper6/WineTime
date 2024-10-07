package seng202.team1.services;

import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service class used by MainController to store categories that have been generated in order
 * to reduce wait times.
 */
public class CategoryService {
    private static final List<Parent> allCategories = new ArrayList<>();
    private static boolean categoriesGenerated = false;

    /**
     * Returns a list of generated wine categories.
     * @return List of Parent objects representing wine categories.
     */
    public static List<Parent> getAllCategories() {
        return allCategories;
    }

    /**
     * Returns whether the categories have been generated yet or not.
     * @return boolean representing whether the categories have been generated.
     */
    public static boolean isCategoriesGenerated() {
        return categoriesGenerated;
    }

    /**
     * Sets whether the categories have been generated or not.
     * @param generated boolean representing whether the categories have been generated.
     */
    public static void setCategoriesGenerated(boolean generated) {
        categoriesGenerated = generated;
    }

    /**
     * Resets the categories list and sets categoriesGenerated to false.
     */
    public static void resetCategories() {
        allCategories.clear();
        categoriesGenerated = false;
    }
}