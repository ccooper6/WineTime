package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DAOs.TagDAO;
import seng202.team1.repository.DatabaseManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TagDAOTest {

    @Test
    public void testGetVarieties() {
        ArrayList<String> varieties = TagDAO.getInstance().getVarieties();
        assertEquals(699, varieties.size());
    }

    @Test
    public void testGetWineries() {
        ArrayList<String> wineries = TagDAO.getInstance().getWineries();
        assertEquals(16740, wineries.size());
    }

    @Test
    public void testGetCountries() {
        ArrayList<String> countries = TagDAO.getInstance().getCountries();
        assertEquals(42, countries.size());
    }
}
