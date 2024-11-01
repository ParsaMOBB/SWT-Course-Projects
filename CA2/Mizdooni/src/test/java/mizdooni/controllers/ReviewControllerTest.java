package mizdooni.controllers;
import mizdooni.exceptions.RestaurantNotFound;
import mizdooni.model.Restaurant;
import mizdooni.model.Review;
import mizdooni.model.User;
import mizdooni.model.Rating;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    User costumer;
    User manager;
    Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        costumer = new User("Arshia", "aa", "arshiya.ataei81@gmail.com", new Address("Iran", "Tehran", "Amirabad"), User.Role.client);
        manager = new User("Mobed", "bb", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.manager);
        restaurant = new Restaurant("Little", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
    }

    @Test
    void getReviews_success() {
        int restaurantId = restaurant.getId();
        int page = 1;
        
        PagedList<Review> pagedReviews = new PagedList<>(List.of(new Review(costumer, new Rating(), "bad comment!", LocalDateTime.now())), 1, 1);
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);
        try {
            when(reviewService.getReviews(restaurantId, page)).thenReturn(pagedReviews);
        } catch (RestaurantNotFound e) {
            throw new RuntimeException(e);
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

}
