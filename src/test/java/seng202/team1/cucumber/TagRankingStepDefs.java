package seng202.team1.cucumber;

import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;

public class TagRankingStepDefs {
    static ReviewService reviewService;
    static LogWineDao logWineDao;
    private int uid;
    private void initialize() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        reviewService = new ReviewService();
        logWineDao = new LogWineDao();
    }
}
