package seng202.team1.unittests.services;

import org.junit.jupiter.api.Test;
import seng202.team1.services.WineCategoryService;

import static org.junit.jupiter.api.Assertions.*;

public class WineCategoryServiceTest {

    @Test
    void testServiceNotNull() {
        assertNotNull(WineCategoryService.getInstance());
    }

    @Test
    void testTitleDoesNotContainCommas() {
        WineCategoryService wineCategoryService = WineCategoryService.getInstance();
        wineCategoryService.setCurrentCategoryTitle("Red, White, Blue");
        assertFalse(wineCategoryService.getCurrentCategoryTitle().contains(","));
    }

    @Test
    void testServiceCorrectlyFormatsTitle() {
        WineCategoryService wineCategoryService = WineCategoryService.getInstance();
        wineCategoryService.setCurrentCategoryTitle("Red, White, Blue");
        assertEquals("Red White Blue", wineCategoryService.getCurrentCategoryTitle());
    }

    @Test
    void testChangeCategoryTitle() {
        WineCategoryService wineCategoryService = WineCategoryService.getInstance();
        wineCategoryService.setCurrentCategoryTitle("Red, White, Blue");
        assertEquals("Red White Blue", wineCategoryService.getCurrentCategoryTitle());
        wineCategoryService.setCurrentCategoryTitle("White");
        assertEquals("White", wineCategoryService.getCurrentCategoryTitle());
    }

    @Test
    void testMakingSureTheServiceIsSingleton() {
        WineCategoryService wineCategoryService1 = WineCategoryService.getInstance();
        WineCategoryService wineCategoryService2 = WineCategoryService.getInstance();

        wineCategoryService1.setCurrentCategoryTitle("Red, White, Blue");
        wineCategoryService2.setCurrentCategoryTitle("White");

        assertEquals(wineCategoryService1.getCurrentCategoryTitle(), "White");
        assertEquals(wineCategoryService2.getCurrentCategoryTitle(), "White");
    }
}
