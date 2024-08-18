package seng202.team0.repository;

import seng202.team0.exceptions.DuplicateEntryException;
import seng202.team0.models.Wine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class WineDAO implements DAOInterface<Wine> {
    @Override
    public List getAll() {
        return null;
    }

    @Override
    public Wine getOne(int id) {
        return null;
    }

    @Override
    public int add(Wine toAdd) throws DuplicateEntryException {
        return 0;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Wine toUpdate) {

    }

    /**
     * Called upon first run of jar, fills the database created by {@link DatabaseManager} with the initial
     * 130k wines.
     */
    public void initializeAllWines() {
        //TODO: Please fix file path when building! "/csvFiles/Wine130k.csv"
        String winePath = "src/main/resources/csvFiles/WineNoDescription130k.csv";
        String wineDescriptionPath = "src/main/resources/csvFiles/WineDescription130k.csv";
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(winePath));
            BufferedReader br2 = new BufferedReader(new FileReader(wineDescriptionPath));
            int i = 0;
            while ((line = br.readLine()) != null && i < 10) {
                if (i != 0) {
                    String description = br2.readLine();
                    String[] wineValues = line.split(",");
                    System.out.println("Wine id no: "+wineValues[0]+" Wine description: "+description);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        WineDAO wineDAO = new WineDAO();
        wineDAO.initializeAllWines();
    }
}
