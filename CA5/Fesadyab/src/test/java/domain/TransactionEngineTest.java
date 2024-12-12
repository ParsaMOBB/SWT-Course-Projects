package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionEngineTest {
    TransactionEngine transactionEngine;

    @BeforeEach
    void setup() {
        transactionEngine = new TransactionEngine();
    }

    @Test
    void testGetAverageTransactionAmountByAccount_EmptyTransactionHistory() {
        assertEquals(0, transactionEngine.getAverageTransactionAmountByAccount(1));
    }

    @Test
    void testGetAverageTransactionAmountByAccount_WithOtherAccountsTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setAccountId(1);
        transaction2.setAmount(50);
        transaction2.setDebit(true);

        Transaction transaction3 = new Transaction();
        transaction3.setTransactionId(3);
        transaction3.setAccountId(2);
        transaction3.setAmount(150);
        transaction3.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        transactionEngine.addTransactionAndDetectFraud(transaction2);
        transactionEngine.addTransactionAndDetectFraud(transaction3);

        assertEquals(75, transactionEngine.getAverageTransactionAmountByAccount(1));
    }


    @Test
    void testGetTransactionPatternAboveThreshold_EmptyTransactionHistory() {
        assertEquals(0, transactionEngine.getTransactionPatternAboveThreshold(50));
    }

    @Test
    void testGetTransactionPatternAboveThreshold_WithPattern_WithoutBelowTresholdTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setAccountId(1);
        transaction2.setAmount(50);
        transaction2.setDebit(true);

        Transaction transaction3 = new Transaction();
        transaction3.setTransactionId(3);
        transaction3.setAccountId(2);
        transaction3.setAmount(150);
        transaction3.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        transactionEngine.addTransactionAndDetectFraud(transaction2);
        transactionEngine.addTransactionAndDetectFraud(transaction3);

        assertEquals(50, transactionEngine.getTransactionPatternAboveThreshold(50));
    }

    @Test
    void testGetTransactionPatternAboveThreshold_WithPattern_WithBelowTresholdTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setAccountId(1);
        transaction2.setAmount(150);
        transaction2.setDebit(true);

        Transaction transaction3 = new Transaction();
        transaction3.setTransactionId(3);
        transaction3.setAccountId(2);
        transaction3.setAmount(50);

        Transaction transaction4 = new Transaction();
        transaction4.setTransactionId(4);
        transaction4.setAccountId(1);
        transaction4.setAmount(200);
        transaction4.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        transactionEngine.addTransactionAndDetectFraud(transaction2);
        transactionEngine.addTransactionAndDetectFraud(transaction3);
        transactionEngine.addTransactionAndDetectFraud(transaction4);

        assertEquals(50, transactionEngine.getTransactionPatternAboveThreshold(50));
    }

    @Test
    void testGetTransactionPatternAboveThreshold_WithoutPattern() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setAccountId(1);
        transaction2.setAmount(150);
        transaction2.setDebit(true);

        Transaction transaction3 = new Transaction();
        transaction3.setTransactionId(3);
        transaction3.setAccountId(2);
        transaction3.setAmount(50);

        Transaction transaction4 = new Transaction();
        transaction4.setTransactionId(4);
        transaction4.setAccountId(1);
        transaction4.setAmount(200);
        transaction4.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        transactionEngine.addTransactionAndDetectFraud(transaction2);
        transactionEngine.addTransactionAndDetectFraud(transaction3);
        transactionEngine.addTransactionAndDetectFraud(transaction4);

        assertEquals(0, transactionEngine.getTransactionPatternAboveThreshold(40));
    }

    @Test
    void testDetectFraudulentTransaction_ExcessiveDebit() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setAccountId(1);
        transaction2.setAmount(300);
        transaction2.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        assertEquals(100, transactionEngine.detectFraudulentTransaction(transaction2));
    }

    @Test
    void testDetectFraudulentTransaction_NotExcessiveDebit() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setAccountId(1);
        transaction2.setAmount(200);
        transaction2.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        assertEquals(0, transactionEngine.detectFraudulentTransaction(transaction2));
    }

    @Test
    void testAddTransactionAndDetectFraud_NewTransaction() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(false);

        assertEquals(0, transactionEngine.addTransactionAndDetectFraud(transaction1));
        assertEquals(1, transactionEngine.transactionHistory.size());
    }

    @Test
    void testAddTransactionAndDetectFraud_FraudulentTransaction() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setAccountId(1);
        transaction2.setAmount(300);
        transaction2.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        assertEquals(100, transactionEngine.addTransactionAndDetectFraud(transaction2));
        assertEquals(2, transactionEngine.transactionHistory.size());
    }

    @Test
    void testAddTransactionAndDetectFraud_DuplicateTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1);
        transaction1.setAccountId(1);
        transaction1.setAmount(100);
        transaction1.setDebit(true);

        transactionEngine.addTransactionAndDetectFraud(transaction1);
        transactionEngine.addTransactionAndDetectFraud(transaction1);
        assertEquals(1, transactionEngine.transactionHistory.size());
    }
}
