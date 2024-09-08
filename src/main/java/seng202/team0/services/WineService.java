package seng202.team0.services;/*package seng202.team0.services;
package seng202.team0.services;

import seng202.team0.models.Tag;
import seng202.team0.models.Wine;
import seng202.team0.repository.WineDAO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WineService {
    private WineDAO wineDAO = new WineDAO();

    public List<Wine> searchWineByName(String wineName) {
        return wineDAO.getWinesByName(wineName);
    }

    public List<Wine> getRandomWines(int number) {
        return wineDAO.getRandomWines(number);
    }

    public void populateDataUsingTags(Wine wine) {
        ArrayList<Tag> tags = wineDAO.getWineTags(wine);

        for (Tag tag : tags) {
            switch (tag.getType()) {
                case "Vintage":
                    wine.setVintage(Integer.parseInt(tag.getName()));
                    break;
                case "Variety":
                    wine.setVariety(tag.getName());
                    break;
                case "Country":
                    wine.setCountry(tag.getName());
                    break;
                case "Province":
                    wine.setProvince(tag.getName());
                    break;
                case "Winery":
                    wine.setWinery(tag.getName());
                    break;
                case "Region":
                    wine.setRegion(tag.getName());
                    break;
            }
        }
    }
}