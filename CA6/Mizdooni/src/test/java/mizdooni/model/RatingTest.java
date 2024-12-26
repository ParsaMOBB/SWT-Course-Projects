package mizdooni.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RatingTest {

    @ParameterizedTest
    @CsvSource({
        "4.6, 5", // rounded up to 5, but clamped to max 5
        "4.4, 4", // rounded to 4
        "3.2, 3", // rounded to 3
        "2.5, 3", // rounded up to 3
        "0.0, 0", // zero test
        "0.1, 0", // rounded to 0
        "5.7, 5", // capped at 5
        "10.0, 5" // capped at 5
    })
    public void testGetStarCount(double overall, int expectedStarCount) {
        Rating rating = new Rating();
        rating.overall = overall;
        assertEquals(expectedStarCount, rating.getStarCount());
    }
}
