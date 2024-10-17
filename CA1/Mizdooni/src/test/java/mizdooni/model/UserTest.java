package model;
import mizdooni.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User arshia;
    private User manager;
    private Reservation reservation1;
    private Reservation reservation2;
    private Restaurant restaurant1;
    private Table table1;
    @BeforeEach
    public void setup(){
        arshia = new User("Arshia", "aa", "arshiya.ataei81@gmail.com", new Address("Iran", "Tehran", "Amirabad"), User.Role.manager);
        manager = new User("Mobed", "bb", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.manager);
        restaurant1 = new Restaurant("Little", manager, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim", new Address("Iran", "Tehran", "Vanak Park"), "Nadarim");
        reservation1 = new Reservation(arshia, restaurant1, new Table(1, restaurant1.getId(), 3), LocalDateTime.of(2024, 10, 2, 19, 0, 0));
    }

    @Test
    public void reservation_successfully_added_test(){
        arshia.addReservation(reservation1);
        List<Reservation> reservations = arshia.getReservations();
        assertEquals(reservations.size(), 1);
        assertEquals(reservation1.getReservationNumber(), reservations.get(0).getReservationNumber());
    }

    @Test
    public void

}
