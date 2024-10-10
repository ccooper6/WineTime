package seng202.team1.unittests.services;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import seng202.team1.services.QuizService;

import java.util.ArrayList;
import java.util.Arrays;
import seng202.team1.models.Wine;

import static org.junit.Assert.assertTrue;

public class QuizServiceTest {

    QuizService quizService = new QuizService();

    @Test
    public void testAllQuizAnswersReturnAWine()
    {
        Wine wine = null;

        boolean hadNoNullWine = true;

        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                for (int k = 1; k < 5; k++) {
                    quizService.setRecordOfAnswers(new ArrayList<>(Arrays.asList(i, j, 1, k, 1)));
                    quizService.wineAlgorithm();
                    wine = quizService.getWine();
                    hadNoNullWine &= (wine != null);
                }
            }
        }

        assertTrue(hadNoNullWine);
    }


}
