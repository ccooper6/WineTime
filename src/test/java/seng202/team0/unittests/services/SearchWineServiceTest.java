package seng202.team0.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.exceptions.InstanceAlreadyExistsException;
import seng202.team0.models.Wine;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.UserDAO;
import seng202.team0.services.SearchWineService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchWineServiceTest {

    private static DatabaseManager databaseManager;

    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException
    {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
    }

    /**
     * Tests searches for all wines with names including 'rose'
     */
    @Test
    public void searchWinesByTagGeneral1()
    {
        ArrayList<String> tags = new ArrayList<>(List.of("pinot noir", "oregon"));
        ArrayList<Wine> fromDB = SearchWineService.getInstance().searchWinesByTags(tags);
    }
}
