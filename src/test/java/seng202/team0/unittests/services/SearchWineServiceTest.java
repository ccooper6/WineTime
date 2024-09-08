package seng202.team0.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.exceptions.InstanceAlreadyExistsException;
import seng202.team0.models.Wine;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.UserDAO;
import seng202.team0.services.SearchWineService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchWineServiceTest {

    private static DatabaseManager databaseManager;

    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException
    {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
    }

    /**
     * Tests searches for all wines with names including 'rose'
     */
    @Test
    public void searchWinesByTagGeneral1()
    {
        String tags = "Oregon,Pinot Noir";
        SearchWineService.getInstance().searchWinesByTags(tags);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(2557, fromDB.size());
        assertNotNull(fromDB);
    }

    @Test
    public void searchWinesByTagGeneral2()
    {
        String tags = "  Oregon  , Pinot Noir  ";
        SearchWineService.getInstance().searchWinesByTags(tags);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(2557, fromDB.size());
        assertNotNull(fromDB);
    }

    @Test
    public void searchWinesByTagGeneral3()
    {
        String tags = "2030";
        SearchWineService.getInstance().searchWinesByTags(tags);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
        assertNotNull(fromDB);

    }

    @Test
    public void searchWinesByTagGeneral4()
    {
        String tags = "2001,2002";
        SearchWineService.getInstance().searchWinesByTags(tags);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
        assertNotNull(fromDB);

    }
}
