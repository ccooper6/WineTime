package seng202.team1.unittests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.WineDAO;

import static org.junit.jupiter.api.Assertions.*;

public class WineDAOTest {

    static DatabaseManager databaseManager;
    static WineDAO wineDAO;
    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException{
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineDAO = new WineDAO();
    }

    @BeforeEach
    void resetDB(){
        databaseManager.resetDB();
    }

    /*@Test
    public void testGetOneGoodID(){
        wineDAO.initializeAllWines();
        assertNotNull(wineDAO.getOne(72));
    }*/

    @Test
    public void testGetOneBadID(){
        wineDAO.initializeAllWines();
        assertNull(wineDAO.getOne(72727272));

    }

    @Test
    public void testVarietyTagsInSet(){
        wineDAO.initializeAllWines();
        for (String tag: wineDAO.getVarietyTags()) {
            if (!(wineDAO.getWhite().contains(tag) || wineDAO.getRed().contains(tag) || wineDAO.getRose().contains(tag) || wineDAO.getSparkling().contains(tag))) {
                System.out.println(tag);
            }
        }
    }
}
