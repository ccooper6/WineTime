package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.SearchDAO;
import seng202.team1.services.SearchWineService;
import seng202.team1.repository.SearchDAO;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void searchWinesByTags1Tag()
    {
        String tags = "Oregon";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        boolean isCorrectLength = fromDB.size() == 4921;

        boolean hasCorrectTags = true;

        for (Wine wine : fromDB) {
            if (!wine.hasLocation("Oregon")) {
                hasCorrectTags = false;
                break;
            }
        }

        boolean didPassTest = isCorrectLength && hasCorrectTags;

        assertTrue(didPassTest);
    }

    @Test
    public void searchWinesByTagsEmptyString()
    {
        String tags = "";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByTagsTrailingComma()
    {
        String tags = "Oregon,";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(4921, fromDB.size());
    }

    @Test
    public void searchWinesByTagsEmptyTag()
    {
        String tags = "Oregon,,Pinot Noir";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByTag2Tags()
    {
        String tags = "Oregon,Pinot Noir";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(2557, fromDB.size());
    }

    @Test
    public void searchWinesByTag2TagsWeirdText()
    {
        String tags = "  oRÉGon  , pinót Noir  ";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(2557, fromDB.size());
    }

    @Test
    public void searchWinesByTagGeneralTagNotExist()
    {
        String tags = "2030";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByTagGeneralConflictingTags()
    {
        String tags = "2001,2002";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByName() {
        String name = "Chardonnay";
        SearchWineService.getInstance().searchWinesByName(name, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(8886, fromDB.size());
    }
}
