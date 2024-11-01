package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;


public class TableTest {

    private Restaurant restaurant1;
    private Table table1;
    private User manager1;
    private User customer1;
    

    @BeforeEach
    public void setup(){
        restaurant1 = new Restaurant("Little", manager1, "Pizza", LocalTime.parse("08:00"), LocalTime.parse("22:00"), "Pizza midim, Napolitan!", new Address("Iran", "Tehran", "Vanak Park"), "Chara Nadarim ?");
        table1 = new Table(1, restaurant1.getId(), 4);
        manager1 = new User("Arshia", "aa", "arshia@gmail.com", new Address("Iran", "Tehran", "Amir Abad"), User.Role.manager);
        customer1 = new User("Mobed", "Khake Pa", "mobed@gmail.com", new Address("Iran", "Tehran", "Valfajr"), User.Role.client);

    }

    @Test
    public void add_reservation_test() {
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        Reservation reservation = new Reservation(customer1, restaurant1, table1, date);
        table1.addReservation(reservation);
        assert(table1.getReservations().contains(reservation));
    }

    @Test
    public void add_two_reservations_same_table_test() {
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2024, 10, 3, 19, 0, 0);
        Reservation reservation = new Reservation(customer1, restaurant1, table1, date);
        table1.addReservation(reservation);
        Reservation reservation2 = new Reservation(customer1, restaurant1, table1, date2);
        table1.addReservation(reservation2);
        assert(table1.getReservations().size() == 2);
    }

    @Test
    public void is_reserved_no_reservation_test() {
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        assert(!table1.isReserved(date));
    }


    @Test
    public void is_reserved_one_reservation_test() {
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        Reservation reservation = new Reservation(customer1, restaurant1, table1, date);
        table1.addReservation(reservation);
        assert(table1.isReserved(date));
    }

    @Test
    public void is_reserved_cancelled_reservation_test() {
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        Reservation reservation = new Reservation(customer1, restaurant1, table1, date);
        reservation.cancel();
        table1.addReservation(reservation);
        assert(!table1.isReserved(date));
    }

    @Test
    public void is_reserved_different_date_test() {
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2024, 10, 3, 19, 0, 0);
        Reservation reservation = new Reservation(customer1, restaurant1, table1, date);
        table1.addReservation(reservation);
        assert(!table1.isReserved(date2));
    }

    @Test
    public void is_reserved_different_table_test() {
        LocalDateTime date = LocalDateTime.of(2024, 10, 2, 19, 0, 0);
        Table table2 = new Table(2, restaurant1.getId(), 4);
        Reservation reservation = new Reservation(customer1, restaurant1, table1, date);
        table1.addReservation(reservation);
        assert(!table2.isReserved(date));
    }

}
