package seng202.team1.services;

import seng202.team1.models.Wine;
import seng202.team1.repository.SearchDAO;

import java.util.ArrayList;
import java.util.List;

public class ChallengeService {

    SearchDAO searchDAO = new SearchDAO();

    List<String> varietyTags = List.of("Pinot Gris", "Chardonnay", "Fumé Blanc", "Merlot", "Mourvèdre",
            "Port", "Cabernet Franc", "Rosé", "Sherry", "Prosecco");
//

    private final ArrayList<Wine> wines = new ArrayList<>();

//    this is the tags of white wines, need to get the wines from the db using these tags.
    public void getTheWines() {
        for (int i = 0; i < 5; i++) {
            ArrayList<String> tag = new ArrayList<>();
            tag.add(varietyTags.get(i));
            ArrayList<Wine> tagWines = searchDAO.searchWineByTags(tag, 1);
            System.out.println(tagWines.getFirst());
        }
    }

//        if (tagWines.size() >0) {
//            System.out.print(tagWines.get(0).getName());
//            wines.add(tagWines.get(0));
//        }
////            needs the wines id not just the tags, probably need to make method in wineDAO to get the wines with the
////            correct tags.
//        }
//        for(int i = 0; i <wines.size(); i++) {
//            System.out.println(wines.get(i).getName());
//        }

//    }


    /*
  when a challenge is started.
  move the winespane down on the profile screen
  activate the challenge pane on the profile screen
  setVisible(true)
  update the users database with a challenge they have joined and their progress
*/
    public void startChallenge() {
//        need to make sure i actually am initializing profile controller
//        when this is called, the current sub-page is the profile, need to use the contriller for this.

//        System.out.println("start challenge was called and tried to move window");
//        profileController.activateChallengPane();
//        update database

    }




    public static void main(String[] args) {

//        ChallengeService challengeService = new ChallengeService();
//        try {
//            challengeService.getTheWines();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
