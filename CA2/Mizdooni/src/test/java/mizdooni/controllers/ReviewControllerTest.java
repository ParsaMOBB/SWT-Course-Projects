package mizdooni.controllers;

import mizdooni.exceptions.InvalidReviewRating;
import mizdooni.exceptions.ManagerCannotReview;
import mizdooni.exceptions.RestaurantNotFound;
import mizdooni.exceptions.UserHasNotReserved;
import mizdooni.exceptions.UserNotFound;
import mizdooni.model.Rating;
import mizdooni.model.Restaurant;
import mizdooni.model.Review;
import mizdooni.model.User;
import mizdooni.model.Address;
import mizdooni.response.PagedList;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private User customer;
    private User manager;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new User("Arshia", "aa", "arshiya.ataei81@gmail.com", new Address("Iran", "Tehran", "Amirabad"), User.Role.client);
        manager = new User("Mobed", "bb", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.manager);
        
        restaurant = new Restaurant("Little", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
    }

    @Test
    void getReviews_success() {
        int restaurantId = restaurant.getId();
        int page = 1;

        PagedList<Review> pagedReviews = new PagedList<>(List.of(new Review(customer, new Rating(4.0, 4.0, 4.0, 4.0), "Good!", LocalDateTime.now())), page, 1);
        try {
            when(reviewService.getReviews(restaurantId, page)).thenReturn(pagedReviews);
        } catch (RestaurantNotFound e) {
            e.printStackTrace();
        }

        Response response = reviewController.getReviews(restaurantId, page);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("reviews for restaurant (" + restaurantId + "): " + restaurant.getName(), response.getMessage());
        assertEquals(pagedReviews, response.getData());
    }

    @Test
    void getReviews_restaurantNotFound() {
        int restaurantId = 1;
        int page = 1;

        when(restaurantService.getRestaurant(restaurantId)).thenReturn(null);

        Exception exception = assertThrows(ResponseException.class, () ->
                reviewController.getReviews(restaurantId, page)
        );

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseException) exception).getStatus());
        assertEquals("restaurant not found", ((ResponseException) exception).getMessage());
    }

    @Test
    void addReview_success() {
        int restaurantId = restaurant.getId();
        Map<String, Object> params = Map.of(
                "comment", "Excellent!",
                "rating", Map.of("food", 4.5, "service", 5.0, "ambiance", 4.0, "overall", 4.5)
        );

        try {
            doNothing().when(reviewService).addReview(eq(restaurantId), any(Rating.class), eq("Excellent!"));
        } catch (UserNotFound | ManagerCannotReview | RestaurantNotFound | InvalidReviewRating | UserHasNotReserved e) {
            e.printStackTrace();
        }

        Response response = reviewController.addReview(restaurantId, params);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("review added successfully", response.getMessage());
    }

    @Test
    void addReview_missingParameters() {
        int restaurantId = restaurant.getId();
        Map<String, Object> params = Map.of(
                "comment", "Excellent!"
        );

        Exception exception = assertThrows(ResponseException.class, () ->
                reviewController.addReview(restaurantId, params)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseException) exception).getStatus());
        assertEquals("parameters missing", ((ResponseException) exception).getMessage());
    }

    @Test
    void addReview_invalidRatingParameter() {
        int restaurantId = restaurant.getId();
        Map<String, Object> params = Map.of(
                "comment", "Good!",
                "rating", Map.of("food", "bad", "service", 5.0, "ambiance", 4.0, "overall", 4.5)  // "food" is invalid
        );

        Exception exception = assertThrows(ResponseException.class, () ->
                reviewController.addReview(restaurantId, params)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseException) exception).getStatus());
        assertEquals("bad parameter type", ((ResponseException) exception).getMessage());
    }
}
