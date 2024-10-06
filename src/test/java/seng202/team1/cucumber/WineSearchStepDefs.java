package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javafx.collections.ArrayChangeListener;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Wine;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.services.SearchWineService;

import static org.apache.commons.collections.CollectionUtils.size;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Objects;

public class WineSearchStepDefs {

    ArrayList<Wine> wineList;
    SearchWineService searchWineService;

    @Given("Wines are stored correctly")
    public void winesAreStoredCorrectly() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        searchWineService = new SearchWineService();
    }

    @When("The user searches for wines with {string} {string}")
    public void userSearches(String type, String filter) {
        if (type.equals("name"))
            searchWineService.searchWinesByName(filter, SearchDAO.UNLIMITED);
        else if (type.equals("tags"))
            searchWineService.searchWinesByTags(filter, SearchDAO.UNLIMITED);
        else
            throw new IllegalArgumentException(type + " must be 'name' or 'tags'");
        wineList = searchWineService.getWineList();
    }

    @Then("The wine list should have {int} wines and all wines should have {string} in their {string}")
    public void winesMatchTheSearch(int size, String filter, String type) {
        boolean didPass = size == wineList.size();

        filter = Normalizer.normalize(filter, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();

        if (type.equals("name")) {

            for (Wine wine : wineList) {
                String wineName = Normalizer.normalize(wine.getName(), Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
                if (!wineName.contains(filter)) {
                    didPass = false;
                    break;
                }
            }
        } else if (type.equals("tags")) {
            String[] tagsArray = filter.split(",");

            ArrayList<String> tagList = new ArrayList<>();
            for (String tag : tagsArray) {
                tagList.add(tag.trim());
            }

            for (Wine wine : wineList) {
                if (!didPass)
                    break;

                for (String tag : tagList) {
                    if (!wine.hasTag(tag)) {
                        didPass = false;
                        break;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException(type + " must be 'name' or 'tags'");
        }

        assertTrue(didPass);
    }


    @When("The user searches for wines with query:{string}, country:{string}, winery:{string}, variety:{string}, minpoints:{int}, maxpoints:{int}, minyear:{int}, maxyear:{int}\"")
    public void userSearches(String filter, String country, String winery, String variety, int minPoints, int maxPoints, int minYear, int maxYear) {

        if (Objects.equals(country, "")) {
            searchWineService.setCurrentCountryFilter(null);
        } else {
            searchWineService.setCurrentCountryFilter(country);
        }
        if (Objects.equals(winery, "")) {
            searchWineService.setCurrentWineryFilter(null);
        } else {
            searchWineService.setCurrentWineryFilter(winery);
        }
        if (Objects.equals(variety, "")) {
            searchWineService.setCurrentVarietyFilter(null);
        } else {
            searchWineService.setCurrentVarietyFilter(variety);
        }

        searchWineService.setCurrentMinPoints(minPoints);
        searchWineService.setCurrentMaxPoints(maxPoints);
        searchWineService.setCurrentMinYear(minYear);
        searchWineService.setCurrentMaxYear(maxYear);
        FXWrapper.getInstance().setCurrentPage("searchWine");
        searchWineService.searchWinesByName(filter, SearchDAO.UNLIMITED);
        wineList = searchWineService.getWineList();
        System.out.println(wineList);
    }

    @Then("The wine list should have {int} wines and all wines should have {string} in their name")
    public void winesMatchTheSearchName(int size, String filter) {
        boolean didPass = size == wineList.size();

        filter = Normalizer.normalize(filter, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();

        for (Wine wine : wineList) {
            String wineName = Normalizer.normalize(wine.getName(), Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (!wineName.contains(filter)) {
                didPass = false;
                break;
            }
        }
        assertTrue(didPass);
    }

    @Then("The wine list should have {int} wines and all wines should have {string} in their name and tags between {string} {int} and {int}")
    public void winesMatchTheSearchNameAndRange(int size, String filter, String type, int lowerBound, int upperBound) {
        boolean didPass = size == wineList.size();
        System.out.println(didPass);
        filter = Normalizer.normalize(filter, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();

        for (Wine wine : wineList) {
            String wineName = Normalizer.normalize(wine.getName(), Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (!wineName.contains(filter)) {
                didPass = false;
                break;
            }
            if (Objects.equals(type, "year")) {
                if (wine.getVintage() < lowerBound || wine.getVintage() > upperBound) {
                    didPass = false;
                    break;
                }
//            } else if (Objects.equals(type, "points")) {
//                if (wine.getPoint() < lowerBound || wine.getVintage() > upperBound) {
//                    didPass = false;
//                    break;
//                }
            } else {
                throw new IllegalArgumentException(type + " must be 'year' or 'points'");
            }
        }
        assertTrue(didPass);
    }


    @Then("The wine list should have {int} wines and all wines should have {string} in their name and {string} in their tags")
    public void winesMatchTheSearchName (int size, String filter, String tag) {
        boolean didPass = size == wineList.size();

        filter = Normalizer.normalize(filter, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();

        for (Wine wine : wineList) {
            String wineName = Normalizer.normalize(wine.getName(), Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (!wineName.contains(filter)) {
                didPass = false;
                break;
            }
            if (!wine.hasTag(tag)) {
                didPass = false;
                break;
            }
        }
        assertTrue(didPass);
    }
}