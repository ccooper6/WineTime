package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.UserLoginService;

import java.text.Normalizer;
import java.util.ArrayList;


public class SearchDAOTest {
    public static SearchDAO searchDAO;
    public static DatabaseManager databaseManager;
    @BeforeEach
    void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        searchDAO = new SearchDAO();
    }

    @Test
    public void testSearchByNameAndFilter() {
//        String tags = "";
//        tags = Normalizer.normalize(tags, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
//        String[] tagsArray = tags.split(",");
//
//        System.out.println(tagsArray.length);
//
        ArrayList<String> tagList = new ArrayList<>();
//        for (String tag : tagsArray) {
//            tagList.add(tag.trim());
//        }

        System.out.println(SearchDAO.getInstance().searchByNameAndFilter(tagList, 0, 100, 0, 3000, null, SearchDAO.UNLIMITED, "wine_id"));
    }
}
