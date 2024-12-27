package mizdooni.stepDefinition;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import mizdooni.model.*;

import java.io.Console;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StepDefinition {

    private User user;
    private Restaurant restaurant;
    private Reservation reservation;
    private Review review;
    private Rating rating;

    @Given("a first user")
    public void a_first_user() {
        user = createUser("arshia", "arshia.ataei81@gmail.com", User.Role.client);
    }

    @Given("a second user")
    public void a_second_user() {
        user = createUser("parsa", "parsa@gmail.com", User.Role.client);
    }

    @Given("a restaurant")
    public void a_restaurant_named_managed_by_user() {
        User manager = createUser("mobed", "mobed@gmail.com", User.Role.manager);
        restaurant = createRestaurant("Little Italy", manager);
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

    @Given("a review with food {double}, service {double}, ambiance {double}, and overall {double}")
    public void a_review_with_food_service_ambiance_and_overall(double food, double service, double ambiance, double overall) {
        rating = new Rating(food, service, ambiance, overall);
        review = createReview("Bad bood");
    }

    @When("the user adds the review to the restaurant")
    public void the_user_adds_the_review_to_the_restaurant() {
        restaurant.addReview(review);
        System.out.println(review.getRating().food);
    }

    @Then("the restaurant should have the review in its reviews list")
    public void the_restaurant_should_have_the_review_in_its_reviews_list() {
        assertTrue(restaurant.getReviews().contains(review));
    }

    @When("the average rating is calculated for the restaurant")
    public void the_average_rating_is_calculated_for_the_restaurant() {
        rating = restaurant.getAverageRating();
    }

    @Then("the average rating should be food {double}, service {double}, ambiance {double}, and overall {double}")
    public void the_average_rating_should_be_food_service_ambiance_and_overall(double food, double service, double ambiance, double overall) {
        Rating averageRating = restaurant.getAverageRating();
        assertEquals(food, averageRating.food, "Food rating does not match");
        assertEquals(service, averageRating.service, "Service rating does not match");
        assertEquals(ambiance, averageRating.ambiance, "Ambiance rating does not match");
        assertEquals(overall, averageRating.overall, "Overall rating does not match");
    }

    @Then("the user should have the restaurant reserved")
    public void the_user_should_have_the_restaurant_reserved() {
        assertTrue(user.checkReserved(restaurant), "The user does not have the restaurant reserved.");
    }

    @Then("the reservation list of the user should contain {int} reservations")
    public void the_reservation_list_of_the_user_should_contain_reservations(int count) {
        assertEquals(count, user.getReservations().size(), "The reservation count does not match.");
    }

    @Then("the restaurant should only have {int} review")
    public void the_restaurant_should_only_have_review(int count) {
        assertEquals(count, restaurant.getReviews().size(), "The review count does not match.");
    }

    @Then("the restaurant should have {int} reviews in its reviews list")
    public void the_restaurant_should_have_reviews_in_its_reviews_list(int count) {
        assertEquals(count, restaurant.getReviews().size(), "The review count does not match.");
    }

    @Then("the review in the list should have food {double}, service {double}, ambiance {double}, and overall {double}")
    public void the_review_in_the_list_should_have_food_service_ambiance_and_overall(double food, double service, double ambiance, double overall) {
        Review currentReview = restaurant.getReviews().get(0);
        Rating currentRating = currentReview.getRating();

        assertEquals(food, currentRating.food, "Food rating does not match.");
        assertEquals(service, currentRating.service, "Service rating does not match.");
        assertEquals(ambiance, currentRating.ambiance, "Ambiance rating does not match.");
        assertEquals(overall, currentRating.overall, "Overall rating does not match.");
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
