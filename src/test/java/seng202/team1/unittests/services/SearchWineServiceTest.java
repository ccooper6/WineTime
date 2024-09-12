package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.SearchWineService;

import java.util.ArrayList;

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

    @Test
    void testDBConnection() {
        assertNotNull(databaseManager);
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

    @Test
    public void searchWinesByName() {
        String name = "Chardonnay";
        SearchWineService.getInstance().searchWinesByName(name);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(8886, fromDB.size());
        assertNotNull(fromDB);
    }
}
