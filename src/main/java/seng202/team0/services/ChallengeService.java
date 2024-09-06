package seng202.team0.services;

import com.sun.java.accessibility.util.SwingEventMonitor;
import seng202.team0.repository.WineDAO;
import seng202.team0.models.Wine;

import java.util.ArrayList;
import java.util.Set;

public class ChallengeService {

    WineDAO wineDAO = new WineDAO();

//    Set<String> whites = wineDAO.getWhite();
//    this is the tags of white wines, need to get the wines from the db using these tags.
    public void getTheWines() {
//        List<Wine> challengeWines = new ArrayList<>;
//        challengeWines.add(whites.get(0));

//        for (String wine : whites) {
//            System.out.println(wine);
//        }
    }

    public static void main() {
        (new ChallengeService()).getTheWines();
    }
}
