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
    void testGetImagePathRed() {
        wine.setVariety("Cabernet Sauvignon");
        assertEquals(wine.getImagePath(), getClass().getResource("/images/Red Wine.jpg").toExternalForm());
    }

    @Test
    void testGetImagePathWhite() {
        wine.setVariety("Chardonnay");
        assertEquals(wine.getImagePath(), getClass().getResource("/images/White Wine.jpg").toExternalForm());
    }

    @Test
    void testGetImagePathRose() {
        wine.setVariety("Rosé");
        assertEquals(wine.getImagePath(), getClass().getResource("/images/Rose Wine.jpg").toExternalForm());
    }

    @Test
    void testGetImagePathSparkling() {
        wine.setVariety("Champagne Blend");
        assertEquals(wine.getImagePath(), getClass().getResource("/images/Sparkling Wine.jpg").toExternalForm());
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
    void hasVintageTag() {
        wine.setVintage(2020);
        assertTrue(wine.hasTag("2020"));
    }

    @Test
    void hasCountryTag() {
        wine.setCountry("Test");
        assertTrue(wine.hasTag("Test"));
    }

    @Test
    void hasProvinceTag() {
        wine.setProvince("Test");
        assertTrue(wine.hasTag("Test"));
    }

    @Test
    void hasRegion1Tag() {
        wine.setRegion1("Test");
        assertTrue(wine.hasTag("Test"));
    }

    @Test
    void hasRegion2Tag() {
        wine.setRegion2("Test");
        assertTrue(wine.hasTag("Test"));
    }

    @Test
    void hasVarietyTag() {
        wine.setVariety("Test");
        assertTrue(wine.hasTag("Test"));
    }

    @Test
    void hasWineryTag() {
        wine.setWinery("Test");
        assertTrue(wine.hasTag("Test"));
    }

    @Test
    void hasAllTags() {
        wine.setWinery("Test");
        wine.setCountry("NewIsaacland");
        wine.setProvince("Historia");
        wine.setVintage(2004);
        wine.setRegion1("Hello_SENG201_TeachingTeam");
        wine.setRegion2("Please_Give_Us_An_A+");
        wine.setVariety("please_:)_Have_a_Great_Day!");

        assertTrue(wine.hasTag("Test"));
        assertTrue(wine.hasTag("NewIsaacland"));
        assertTrue(wine.hasTag("Historia"));
        assertTrue(wine.hasTag("2004"));
        assertTrue(wine.hasTag("Hello_SENG201_TeachingTeam"));
        assertTrue(wine.hasTag("Please_Give_Us_An_A+"));
        assertTrue(wine.hasTag("please_:)_Have_a_Great_Day!"));
        assertFalse(wine.hasTag("Gave us a B"));
    }
}
