package seng202.team0.services;

import seng202.team0.models.Wine;
import seng202.team0.models.testWines.*;

import java.util.ArrayList;
import java.util.List;

public class SearchWineService {
    private Wine currentWine;
    private ArrayList<Wine> wineList;

    private static SearchWineService instance;

    /**
    Returns the instance and creates one if none exists.

    @return {@link SearchWineService instance}
     */
    public static SearchWineService getInstance()
    {
        if (instance == null) {
            instance = new SearchWineService();

//            TEST WINES
            instance.setWineList(new ArrayList<>(List.of(new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6(),
                    new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6())));
        }
        return instance;
    }

    /**
     * Returns the current wine
     *
     * @return {@link Wine} currentWine
     */
    public Wine getCurrentWine() {
        return currentWine;
    }

    /**
     * Sets the current wine
     *
     * @param currentWine {@link Wine} currentWine
     */
    public void setCurrentWine(Wine currentWine) {
        this.currentWine = currentWine;
    }

    /**
     * Returns the stored wines list
     *
     * @return {@link ArrayList<Wine>} wines
     */
    public ArrayList<Wine> getWineList() {
        return wineList;
    }

    /**
     * Sets the stored wine list
     *
     * @param wineList {@link ArrayList<Wine>} wines
     */
    public void setWineList(ArrayList<Wine> wineList) {
        this.wineList = wineList;
    }
}
