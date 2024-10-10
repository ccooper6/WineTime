package seng202.team1.unittests.services;

import org.junit.jupiter.api.Test;
import seng202.team1.services.WineCategoryService;

import static org.junit.jupiter.api.Assertions.*;

public class WineCategoryServiceTest {

    @Test
    void testServiceNotNull() {
        assertNotNull(WineCategoryService.getInstance());
    }

    // TODO this is a test? its a getter and setter right?
    @Test
    void testChangeCategoryTitle() {
        WineCategoryService wineCategoryService = WineCategoryService.getInstance();
        wineCategoryService.setCurrentCategoryTitle("Red");
        assertEquals("Red", wineCategoryService.getCurrentCategoryTitle());
        wineCategoryService.setCurrentCategoryTitle("White");
        assertEquals("White", wineCategoryService.getCurrentCategoryTitle());
    }


}
