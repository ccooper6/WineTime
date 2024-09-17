package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.WineVarietyService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WineVarietyServiceTest {

    static WineVarietyService wineVarietyService;
    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        wineVarietyService = WineVarietyService.getInstance();

    }

    @Test
    public void testGoodRedWine() {
        int variety = wineVarietyService.getVarietyFromGrape("Syrah");
        assertEquals(0, variety);
    }

    @Test
    public void testGoodWhiteWine() {
        int variety = wineVarietyService.getVarietyFromGrape("Sauvignon Blanc");
        assertEquals(1, variety);
    }

    @Test
    public void testGoodRoseWine() {
        int variety = wineVarietyService.getVarietyFromGrape("Rosé");
        assertEquals(2, variety);
    }

    @Test
    public void testGoodSparklingWine() {
        int variety = wineVarietyService.getVarietyFromGrape("Prosecco");
        assertEquals(3, variety);
    }

    @Test
    public void testBadWine() {
        int variety = wineVarietyService.getVarietyFromGrape("Isaac is really really cool");
        assertEquals(-1, variety);
    }

    @Test
    public void testNullWine() {
        String wine = null;
        int variety = wineVarietyService.getVarietyFromGrape(wine);
        assertEquals(-1, variety);
    }


}
