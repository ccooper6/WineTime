package seng202.team1.services;

import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Service class used by MainController to store categories that have been generated in order
 * to reduce wait times.
 */
public class CategoryService {
    private static final List<Parent> allCategories = new ArrayList<>();
    private static boolean tagsGenerated = false;
    private static String[] generatedTags;

    /**
     * Default constructor for CategoryService
     */
    public CategoryService(){}

    /**
     * Returns a list of generated wine categories.
     * @return List of Parent objects representing wine categories.
     */
    public static List<Parent> getAllCategories() {
        return allCategories;
    }

    /**
     *generates the tags for the categories.
     */
    public static void generateTags() {
        String[] tags = {
                "Bordeaux, Merlot",
                "Marlborough, Sauvignon Blanc",
                "Tuscany, Sangiovese",
                "Hawke's Bay, Syrah",
                "Spain, Rioja, Tempranillo",
                "Mendoza, Malbec",
                "US, Napa Valley, Cabernet Sauvignon",
                "Central Otago, Pinot Noir, New Zealand",
                "Chianti Classico, Sangiovese",
                "Champagne, Pinot Meunier",
                "Provence, Rosé",
                "Prosecco",
                "Hunter Valley, Semillon",
                "Willamette Valley, Pinot Gris",
                "Burgundy, Chardonnay",
                "Mosel, Riesling",
                "Puglia, Primitivo",
                "South Africa, Chenin Blanc",
                "Alsace, Gewurztraminer",
                "Tuscany, Vermentino",
                "Loire Valley, Sauvignon Blanc",
                "Barossa Valley, Shiraz",
                "Champagne, Chardonnay",
                "Côtes de Provence, Rosé",
                "Finger Lakes, Riesling",
                "Portuguese Sparkling",
                "New Zealand"
        };

        List<String> tagsList = new ArrayList<>(Arrays.asList(tags));
        Collections.shuffle(tagsList);
        tags = tagsList.toArray(new String[0]);
        generatedTags = tags;
        tagsGenerated = true;
    }

    /**
     * gets the generated tag
     * @return the generated tags
     */
    public static String[] getGeneratedTags() {
        return generatedTags;
    }

    /**
     * gets if the tags are generated
     * @return boolean, true if the tags are generated.
     */
    public static boolean areTagsGenerated() {
        return tagsGenerated;
    }


    /**
     * Resets the categories list and sets categoriesGenerated to false.
     * @param fullReset Whether to reset the tags as well.
     */
    public static void resetCategories(Boolean fullReset) {
        allCategories.clear();
        if (fullReset) {
            tagsGenerated = false;
        }
    }

    /**
     * Resets the categories list and sets categoriesGenerated to false.
     */
    public static void resetCategories() {
        allCategories.clear();
    }
}