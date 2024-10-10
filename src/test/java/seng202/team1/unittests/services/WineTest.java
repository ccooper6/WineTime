package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.models.TagType;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;

import static org.junit.jupiter.api.Assertions.*;

public class WineTest {
    private Wine wine;

    @BeforeEach
    void setup() {
        WineBuilder wineBuilder = WineBuilder.genericSetup(1, "TestName", "TestDescription", 5, 5);
        wineBuilder.setTag(TagType.VARIETY, "Früburgunder");
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
        boolean hasAllTags = true;
        wine.setWinery("Test");
        wine.setCountry("NewIsaacland");
        wine.setProvince("Historia");
        wine.setVintage(2004);
        wine.setRegion1("Hello_SENG201_TeachingTeam");
        wine.setRegion2("Please_Give_Us_An_A+");
        wine.setVariety("please_:)_Have_a_Great_Day!");
        hasAllTags &= wine.hasTag("Test");
        hasAllTags &= wine.hasTag("NewIsaacland");
        hasAllTags &= wine.hasTag("Historia");
        hasAllTags &= wine.hasTag("2004");
        hasAllTags &= wine.hasTag("Hello_SENG201_TeachingTeam");
        hasAllTags &= wine.hasTag("Please_Give_Us_An_A+");
        hasAllTags &= wine.hasTag("please_:)_Have_a_Great_Day!");
        hasAllTags &= !(wine.hasTag("Doesn't have this tag"));
        assertTrue(hasAllTags);



    }
}
