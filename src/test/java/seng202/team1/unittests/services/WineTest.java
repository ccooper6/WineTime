package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;

import static org.junit.jupiter.api.Assertions.*;

public class WineTest {
    private Wine wine;

    @BeforeEach
    void setup() {
        WineBuilder wineBuilder = WineBuilder.genericSetup(1, "TestName", "TestDescription", 5, resultSet.getInt("points"));
        wineBuilder.setVariety("Früburgunder");
        wine = wineBuilder.build();
    }

    @Test
    void testGetImagePath() {
        assertEquals(wine.getImagePath(), getClass().getResource("/images/Red Wine.jpg").toExternalForm());
    }

    @Test
    void getBadImagePath() {
        wine.setVariety(null);
        assertEquals(wine.getImagePath(), getClass().getResource("/images/wine-bottle_pic.png").toExternalForm());
    }

    @Test
    void testHasNoLocation() {
        assertFalse(wine.hasLocation("FunnyLocation"));
    }

    @Test
    void testHasLocation() {
        wine.setCountry("New Zealand");
        wine.setProvince("Marlborough");
        wine.setRegion1("Awatere Valley");
        wine.setRegion2("Caleb is cool");
        assertTrue(wine.hasLocation("New Zealand"));
        assertTrue(wine.hasLocation("Marlborough"));
        assertTrue(wine.hasLocation("Awatere Valley"));
        assertTrue(wine.hasLocation("Caleb is cool"));
    }

    @Test
    void hasNoTag() {
        assertFalse(wine.hasTag("FunnyTag"));
    }

    @Test
    void hasTag() {
        wine.setWinery("Test");
        assertTrue(wine.hasTag("Test"));
    }
}
