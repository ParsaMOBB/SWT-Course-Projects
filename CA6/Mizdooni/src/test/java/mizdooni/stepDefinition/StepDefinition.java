package mizdooni.stepDefinition;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import mizdooni.model.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StepDefinition {

    private User user;
    private Restaurant restaurant;
    private Reservation reservation;
    private Review review;
    private Rating rating;

    @Given("a user with username {string} and email {string}")
    public void a_user_with_username_and_email(String username, String email) {
        user = createUser(username, email, User.Role.client);
    }

    @Given("a restaurant named {string} managed by user {string}")
    public void a_restaurant_named_managed_by_user(String restaurantName, String managerUsername) {
        User manager = createUser(managerUsername, "mobed@gmail.com", User.Role.manager);
        restaurant = createRestaurant(restaurantName, manager);
    }

    @When("the user adds a reservation to the restaurant")
    public void the_user_adds_a_reservation_to_the_restaurant() {
        reservation = createReservation();
        user.addReservation(reservation);
    }

    @Then("the reservation list of the user should contain the reservation")
    public void the_reservation_list_of_the_user_should_contain_the_reservation() {
        assertTrue(user.getReservations().contains(reservation));
    }

    @Given("a review with food {int}, service {int}, ambiance {int}, and overall {int}")
    public void a_review_with_food_service_ambiance_and_overall(Integer food, Integer service, Integer ambiance, Integer overall) {
        rating = new Rating(food, service, ambiance, overall);
        review = createReview("Bad bood");
    }

    @When("the user adds the review to the restaurant")
    public void the_user_adds_the_review_to_the_restaurant() {
        restaurant.addReview(review);
    }

    @Then("the restaurant should have the review in its reviews list")
    public void the_restaurant_should_have_the_review_in_its_reviews_list() {
        assertTrue(restaurant.getReviews().contains(review));
    }

    @When("the average rating is calculated for the restaurant")
    public void the_average_rating_is_calculated_for_the_restaurant() {
        rating = restaurant.getAverageRating();
    }

    @Then("the average rating should be food {int}, service {int}, ambiance {int}, and overall {int}")
    public void the_average_rating_should_be_food_service_ambiance_and_overall(Integer food, Integer service, Integer ambiance, Integer overall) {
        Rating averageRating = restaurant.getAverageRating();
        assertEquals((int) food, averageRating.food, "Food rating does not match");
        assertEquals((int) service, averageRating.service, "Service rating does not match");
        assertEquals((int) ambiance, averageRating.ambiance, "Ambiance rating does not match");
        assertEquals((int) overall, averageRating.overall, "Overall rating does not match");
    }

    public User createUser(String username, String email,User.Role userRole){
        return new User(username, "aa", email, new Address("Iran", "Tehran", "Amirabad"), userRole);
    }

    public Address createAddress(String street){
        return new Address("Iran", "Tehran", street);
    }

    public Restaurant createRestaurant(String restaurantName, User manager){
        return new Restaurant(restaurantName, manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
    }

    public Table createTable(int id, Restaurant restaurant){
        return new Table(id, restaurant.getId(), 4);
    }

    public Reservation createReservation(){
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        Table table = createTable(1, restaurant);
        return new Reservation(user, restaurant, table, date);
    }

    public Review createReview(String comment){
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        return new Review(user, rating, comment, date);
    }

}
