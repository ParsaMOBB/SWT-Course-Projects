package mizdooni.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantTest {

    Restaurant restaurant;  
    User manager;
    User costumer;

    @BeforeEach
    public void setup(){
        restaurant = new Restaurant("Little", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
        manager = new User("Arshia", "aa", "arshia@gmail.com", new Address("Iran", "Tehran", "Amir Abad"), User.Role.manager);
        costumer = new User("Mobed", "Khake Pa", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.client);
    }       

    @Test
    public void add_table_test() {
        Table table = new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);
        assert(restaurant.getTables().contains(table));
    }

    @Test
    public void add_two_tables_test() {
        Table table = new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);
        Table table2 = new Table(2, restaurant.getId(), 4);
        restaurant.addTable(table2);
        assert(restaurant.getTables().size() == 2);
    }

    @Test
    public void get_table_test() {
        Table table = new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);
        assert(restaurant.getTable(1) == table);
    }

    @Test
    public void get_table_not_found_test() {
        Table table = new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);
        assert(restaurant.getTable(2) == null);
    }

    @Test
    public void get_table_no_table_test() {
        assert(restaurant.getTable(1) == null);
    }

    @Test 
    public void get_tables_no_table_test() {
        assert(restaurant.getTables().size() == 0);
    }

    @Test
    public void add_review_test() {
        Rating rating = new Rating();
        rating.food = 5;
        rating.service = 5;
        rating.ambiance = 5;
        rating.overall = 5;
        Review review = new Review(costumer, rating, "Great food, great service", LocalDateTime.now());
        restaurant.addReview(review);
        assert(restaurant.getReviews().contains(review));
    }

    @Test
    public void add_two_reviews_same_user_test() {
        Rating rating = new Rating();
        rating.food = 5;
        rating.service = 5;
        rating.ambiance = 5;
        rating.overall = 5;
        Review review = new Review(costumer, rating, "Great food, great service", LocalDateTime.now());
        restaurant.addReview(review);
        Review review2 = new Review(costumer, rating, "Great food, great service, great ambience, overall perfect", LocalDateTime.now());
        restaurant.addReview(review2);
        assert(restaurant.getReviews().size() == 1);
        assert(restaurant.getReviews().contains(review2));
    }

    @Test
    public void no_review_test() {
        assert(restaurant.getReviews().size() == 0);
    }

    @Test
    public void get_average_rating_no_review_test() {
        Rating average = restaurant.getAverageRating();
        assert(average.food == 0);
        assert(average.service == 0);
        assert(average.ambiance == 0);
        assert(average.overall == 0);
    }

    @Test
    public void get_average_rating_test() {
        Rating rating = new Rating();
        rating.food = 3;
        rating.service = 3;
        rating.ambiance = 3;
        rating.overall = 3;
        Review review = new Review(costumer, rating, "Mid", LocalDateTime.now());
        restaurant.addReview(review);
        Rating average = restaurant.getAverageRating();
        assert(average.food == 3);
        assert(average.service == 3);
        assert(average.ambiance == 3);
        assert(average.overall == 3);
    }

    @Test
    public void get_star_count_no_review_test() {
        assert(restaurant.getStarCount() == 0);
    }

    @Test
    public void get_star_count_test() {
        Rating rating = new Rating();
        rating.food = 4;
        rating.service = 4;
        rating.ambiance = 5;
        rating.overall = 4.3;
        Review review = new Review(costumer, rating, "Great ambience, not the best food", LocalDateTime.now());
        restaurant.addReview(review);
        assert(restaurant.getStarCount() == 4);
    }

    @Test
    public void get_max_seats_number_no_table_test() {
        assert(restaurant.getMaxSeatsNumber() == 0);
    }

    @Test
    public void get_max_seats_number_test() {
        Table table = new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);
        Table table2 = new Table(2, restaurant.getId(), 6);
        restaurant.addTable(table2);
        Table table3 = new Table(3, restaurant.getId(), 2);
        restaurant.addTable(table3);
        assert(restaurant.getMaxSeatsNumber() == 6);
    }
}
