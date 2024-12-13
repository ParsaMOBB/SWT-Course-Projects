package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {
    Transaction transaction;

    @BeforeEach
    void setup() {
        transaction = new Transaction();
        transaction.setTransactionId(1);
    }

    @Test
    void testEquals_SameIDs() {
        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(1);
        assertNotEquals(transaction, transaction2);
    }

    @Test
    void testEquals_DifferentIDs() {
        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        assertNotEquals(transaction, transaction2);
    }

    @Test
    void testEquals_DifferentClasses() {
        assertNotEquals(new Object(), transaction);
    }
}
