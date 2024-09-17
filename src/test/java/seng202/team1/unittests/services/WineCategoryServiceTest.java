package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.WineCategoryService;
import seng202.team1.services.WineVarietyService;

import static org.junit.jupiter.api.Assertions.*;

public class WineCategoryServiceTest {

    @Test
    void testServiceNotNull() {
        assertNotNull(WineCategoryService.getInstance());
    }

    @Test
    void testIncrementCategory() {
        WineCategoryService.getInstance().resetCurrentCategory();
        int index1 = WineCategoryService.getInstance().getCurrentCategory();
        WineCategoryService.getInstance().incrementCurrentCategory();
        int index2 = WineCategoryService.getInstance().getCurrentCategory();
        String category1 = WineCategoryService.getInstance().getCategoryTitles().get(index1);
        String category2 = WineCategoryService.getInstance().getCategoryTitles().get(index2);
        assertNotEquals(index1, index2);
        assertNotEquals(category1, category2);
    }


}
