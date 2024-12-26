package mizdooni.controllers;

import mizdooni.exceptions.BadPeopleNumber;
import mizdooni.exceptions.DateTimeInThePast;
import mizdooni.exceptions.InvalidManagerRestaurant;
import mizdooni.exceptions.InvalidWorkingTime;
import mizdooni.exceptions.ManagerReservationNotAllowed;
import mizdooni.exceptions.ReservationCannotBeCancelled;
import mizdooni.exceptions.ReservationNotFound;
import mizdooni.exceptions.ReservationNotInOpenTimes;
import mizdooni.exceptions.RestaurantNotFound;
import mizdooni.exceptions.TableNotFound;
import mizdooni.exceptions.UserNoAccess;
import mizdooni.exceptions.UserNotFound;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.Reservation;
import mizdooni.model.Restaurant;
import mizdooni.model.Table;
import mizdooni.model.User;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.ReservationService;
import mizdooni.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private User customer;
    private User manager;
    private Restaurant restaurant;
    private Table table;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new User("Arshia", "aa", "arshiya.ataei81@gmail.com", null, User.Role.client);
        manager = new User("Mobed", "bb", "mobed@gmail.com", null, User.Role.manager);

        restaurant = new Restaurant("Little", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", null, "Chara Nadarim ?");
        table = new Table(1, restaurant.getId(), 4);

        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
    }

    @Test
    void getReservations_success() {
        int restaurantId = restaurant.getId();
        int tableNumber = table.getTableNumber();
        String date = "2024-11-01";
        LocalDate localDate = LocalDate.parse(date);

        List<Reservation> reservations = List.of(new Reservation(customer, restaurant, table, LocalDateTime.now()));
        try {
            when(reservationService.getReservations(restaurantId, tableNumber, localDate)).thenReturn(reservations);
        } catch (RestaurantNotFound | UserNotManager | InvalidManagerRestaurant | TableNotFound e) {
            e.printStackTrace();
        }

        Response response = reservationController.getReservations(restaurantId, tableNumber, date);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("restaurant table reservations", response.getMessage());
        assertEquals(reservations, response.getData());
    }

    @Test
    void getReservations_noDate() {
        int restaurantId = restaurant.getId();
        int tableNumber = table.getTableNumber();

        List<Reservation> reservations = List.of(new Reservation(customer, restaurant, table, LocalDateTime.now()));
        try {
            when(reservationService.getReservations(restaurantId, tableNumber, null)).thenReturn(reservations);
        } catch (RestaurantNotFound | UserNotManager | InvalidManagerRestaurant | TableNotFound e) {
            e.printStackTrace();
        }

        Response response = reservationController.getReservations(restaurantId, tableNumber, null);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("restaurant table reservations", response.getMessage());
        assertEquals(reservations, response.getData());
    }

    @Test
    void getReservations_invalidDateFormat() {
        int restaurantId = restaurant.getId();
        int tableNumber = table.getTableNumber();
        String date = "invalid-date";

        Exception exception = assertThrows(ResponseException.class, () ->
                reservationController.getReservations(restaurantId, tableNumber, date)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseException) exception).getStatus());
        assertEquals("bad parameter type", ((ResponseException) exception).getMessage());
    }

    @Test
    void getCustomerReservations_success() {
        int customerId = customer.getId();

        List<Reservation> reservations = List.of(new Reservation(customer, restaurant, table, LocalDateTime.now()));
        try {
            when(reservationService.getCustomerReservations(customerId)).thenReturn(reservations);
        } catch (UserNotFound | UserNoAccess e) {
            e.printStackTrace();
        }

        Response response = reservationController.getCustomerReservations(customerId);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("user reservations", response.getMessage());
        assertEquals(reservations, response.getData());
    }

    @Test
    void getAvailableTimes_success() {
        int restaurantId = restaurant.getId();
        int people = 4;
        String date = "2024-11-01";
        LocalDate localDate = LocalDate.parse(date);

        List<LocalTime> availableTimes = List.of(LocalTime.of(12, 0), LocalTime.of(14, 0));
        try {
            when(reservationService.getAvailableTimes(restaurantId, people, localDate)).thenReturn(availableTimes);
        } catch (RestaurantNotFound | DateTimeInThePast | BadPeopleNumber e) {
            e.printStackTrace();
        }

        Response response = reservationController.getAvailableTimes(restaurantId, people, date);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("available times", response.getMessage());
        assertEquals(availableTimes, response.getData());
    }

    @Test
    void addReservation_success() {
        int restaurantId = restaurant.getId();
        Map<String, String> params = Map.of(
                "people", "4",
                "datetime", "2024-11-01T12:00"
        );
        LocalDateTime datetime = LocalDateTime.parse("2024-11-01T12:00");

        Reservation reservation = new Reservation(customer, restaurant, table, datetime);
        try {
            when(reservationService.reserveTable(restaurantId, 4, datetime)).thenReturn(reservation);
        } catch (UserNotFound | ManagerReservationNotAllowed | InvalidWorkingTime | RestaurantNotFound | TableNotFound
                | DateTimeInThePast | ReservationNotInOpenTimes e) {
            e.printStackTrace();
        }

        try {
            Response response = reservationController.addReservation(restaurantId, params);
            assertEquals(HttpStatus.OK, response.getStatus());
            assertEquals("reservation done", response.getMessage());
            assertEquals(reservation, response.getData());
        } catch (ResponseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addReservation_missingParameters() {
        int restaurantId = restaurant.getId();
        Map<String, String> params = Map.of(
                "people", "4"
        );

        Exception exception = assertThrows(ResponseException.class, () ->
                reservationController.addReservation(restaurantId, params)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseException) exception).getStatus());
        assertEquals("parameters missing", ((ResponseException) exception).getMessage());
    }

    @Test
    void cancelReservation_success() {
        int reservationNumber = 1;

        try {
            doNothing().when(reservationService).cancelReservation(reservationNumber);
        } catch (UserNotFound | ReservationNotFound | ReservationCannotBeCancelled e) {
            e.printStackTrace();
        }

        Response response = reservationController.cancelReservation(reservationNumber);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("reservation cancelled", response.getMessage());
    }

    @Test
    void cancelReservation_noSuchReservation() {
        int reservationNumber = 1;

        
        try {
            doThrow(new ReservationNotFound()).when(reservationService).cancelReservation(reservationNumber);
        } catch (UserNotFound | ReservationNotFound | ReservationCannotBeCancelled e) {
            e.printStackTrace();
        }

        Exception exception = assertThrows(ResponseException.class, () ->
                reservationController.cancelReservation(reservationNumber)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseException) exception).getStatus());
    }
}
