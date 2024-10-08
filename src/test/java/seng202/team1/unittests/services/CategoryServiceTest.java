package seng202.team1.unittests.services;

import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.controllers.WineCategoryDisplayController;
import seng202.team1.services.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {
    @BeforeEach
    void setUp() {
        CategoryService.resetCategories(true);
    }

    @Test
    void noCategoriesExist() {
        assertTrue(CategoryService.getAllCategories().isEmpty());
    }

    @Test
    void tagsNotGenerated() {
        assertFalse(CategoryService.areTagsGenerated());
    }

    @Test
    void generateTags() {
        CategoryService.generateTags();
        assertFalse(CategoryService.getGeneratedTags().length == 0);
    }

    @Test
    void generateDifferentTags() {
        CategoryService.generateTags();
        String[] tags1 = CategoryService.getGeneratedTags();
        CategoryService.generateTags();
        String[] tags2 = CategoryService.getGeneratedTags();
        assertNotEquals(tags1, tags2);
    }

    @Test
    void generateCategories() {
        Parent parent = new Parent() {};
        CategoryService.getAllCategories().add(parent);
        assertTrue(CategoryService.getAllCategories().contains(parent));
    }

    @Test
    void simulateLogOut() {
        generateCategories();
        assertFalse(CategoryService.getAllCategories().isEmpty());
        CategoryService.resetCategories(true);
        assertTrue(CategoryService.getAllCategories().isEmpty());
        assertFalse(CategoryService.areTagsGenerated());
    }

    @Test
    void simulateReloadMainPage() {
        CategoryService.generateTags();
        String[] tags1 = CategoryService.getGeneratedTags();
        generateCategories();
        assertFalse(CategoryService.getAllCategories().isEmpty());

        CategoryService.resetCategories();

        String[] tags2 = CategoryService.getGeneratedTags();
        assertEquals(tags1, tags2);
        assertTrue(CategoryService.getAllCategories().isEmpty());
    }
}
