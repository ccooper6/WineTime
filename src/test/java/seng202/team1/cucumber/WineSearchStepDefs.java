package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import seng202.team1.models.Wine;
import seng202.team1.services.SearchWineService;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.Normalizer;
import java.util.ArrayList;

public class WineSearchStepDefs {

    ArrayList<Wine> wineList;

    @Given("Wines are stored correctly")
    public void winesAreStoredCorrectly() {
        return;
    }

    @When("The user searches for wines with {string} {string}")
    public void userSearches(String type, String filter)
    {
        if (type.equals("name"))
            SearchWineService.getInstance().searchWinesByName(filter);
        else if (type.equals("tags"))
            SearchWineService.getInstance().searchWinesByTags(filter);
        else
            throw new IllegalArgumentException(type + " must be 'name' or 'tags'");
        wineList = SearchWineService.getInstance().getWineList();
    }

    @Then("The wine list should have {int} wines and all wines should have {string} in their {string}")
    public void winesMatchTheSearch(int size, String filter, String type)
    {
        boolean didPass = size == wineList.size();

        filter = Normalizer.normalize(filter, Normalizer.Form.NFD).replaceAll("^\\p{ASCII}", "").toLowerCase();

        if (type.equals("name")) {

            for (Wine wine : wineList) {
                String wineName = Normalizer.normalize(wine.getName(), Normalizer.Form.NFD).replaceAll("^\\p{ASCII}", "").toLowerCase();
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
}
