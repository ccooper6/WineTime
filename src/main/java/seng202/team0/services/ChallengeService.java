package seng202.team0.services;

import com.sun.java.accessibility.util.SwingEventMonitor;
import seng202.team0.repository.WineDAO;
import seng202.team0.models.Wine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChallengeService {

    WineDAO wineDAO = new WineDAO();

    List<String> varietyTags = List.of("Pinot Gris", "Chardonnay", "Fumé Blanc", "Merlot", "Mourvèdre",
            "Port", "Cabernet Franc", "Rosé", "Sherry", "Prosecco");

    private final ArrayList<Wine> wines = new ArrayList<>();
//    this is the tags of white wines, need to get the wines from the db using these tags.
    public void getTheWines() {
        for(int i = 0; i < varietyTags.size(); i++) {
            wines.add(wineDAO.getWinesFromTags(varietyTags.get(i)).getFirst());
//            needs the wines id not just the tags, probably need to make method in wineDAO to get the wines with the
//            correct tags.
        }
        for(int i = 0; i <wines.size(); i++) {
            System.out.println(wines.get(i).getName());
        }

    }



    public static void main(String[] args) {
        ChallengeService challengeService = new ChallengeService();
        try {
            challengeService.getTheWines();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
