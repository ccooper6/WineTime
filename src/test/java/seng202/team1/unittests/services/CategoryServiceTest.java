package seng202.team1.unittests.services;

import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.services.CategoryService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CategoryServiceTest {
    @BeforeEach
    void setUp() {
        CategoryService.resetCategories();
    }

    @Test
    void noCategoriesExist() {
        assertTrue(CategoryService.getAllCategories().isEmpty());
    }

    @Test
    void categoriesNotGenerated() {
        assertFalse(CategoryService.isCategoriesGenerated());
    }

    @Test
    void addCategory() {
        Parent parent = new Parent() {};
        CategoryService.getAllCategories().add(parent);
        CategoryService.setCategoriesGenerated(true);
        assertTrue(CategoryService.getAllCategories().contains(parent));
        assertTrue(CategoryService.isCategoriesGenerated());
    }

    @Test
    void simulateLogOut() {
        Parent parent = new Parent() {};
        CategoryService.getAllCategories().add(parent);
        CategoryService.setCategoriesGenerated(true);

        CategoryService.resetCategories();
        assertTrue(CategoryService.getAllCategories().isEmpty());
        assertFalse(CategoryService.isCategoriesGenerated());
    }
}
