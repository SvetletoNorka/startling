package bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTests {

    private Client client1;
    private Client client2;
    private Bank bank;

    @BeforeEach
    public void setup() {

        this.client1 = new Client("Client1");
        this.client2 = new Client("Client2");
        this.bank = new Bank("UBB", 2);
    }

    @Test
    public void testAddClient() {
        bank.addClient(client1);
        bank.addClient(client2);
        Assertions.assertEquals(2, bank.getCount());
    }

    @Test
    public void testAddClientNoMoreCapacity() {
        bank.addClient(client1);
        bank.addClient(client2);
        Client client3 = new Client("Client3");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bank.addClient(client3);
        });
        Assertions.assertEquals("The bank has no capacity for more clients!", exception.getMessage());
    }

    @Test
    public void testRemoveNonExistingClient() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bank.removeClient("Gosho");
        });
    }

    @Test
    public void testRemoveClient() {
        bank.addClient(client1);
        bank.addClient(client2);
        Assertions.assertEquals(2, bank.getCount());
        bank.removeClient("Client1");
        Assertions.assertEquals(1, bank.getCount());
    }

    @Test
    public void testLoanWithdrawalNonExistingClient() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bank.loanWithdrawal("Gosho");
        });
    }

    @Test
    public void testLoanWithdrawal() {
        bank.addClient(client1);
        Assertions.assertTrue(client1.isApprovedForLoan());
        Client returnedClient = bank.loanWithdrawal("Client1");
        Assertions.assertEquals(client1, returnedClient);
        Assertions.assertFalse(client1.isApprovedForLoan());
    }

    @Test
    public void testStatistics() {
        bank.addClient(client1);
        String stat = bank.statistics();
        Assertions.assertEquals("The client Client1 is at the UBB bank!", stat);
    }

    @Test
    public void testStatisticsWithMultipleClients() {
        bank.addClient(client1);
        bank.addClient(client2);
        String stat = bank.statistics();
        Assertions.assertEquals("The client Client1, Client2 is at the UBB bank!", stat);
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("UBB", bank.getName());
    }

    @Test
    public void testSetNameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Bank(null, 3);
        });
    }

    @Test
    public void testSetNameEmpty() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Bank("", 3);
        });
    }

    @Test
    public void testSetNameWhitespace() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Bank("   ", 3);
        });
    }

    @Test
    public void testGetCapacity() {
        Assertions.assertEquals(2, bank.getCapacity());
    }

    @Test
    public void testSetCapacityNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Bank("TestBank", -1);
        });
    }

    @Test
    public void testSetCapacityZero() {
        Bank bankWithZeroCapacity = new Bank("TestBank", 0);
        Assertions.assertEquals(0, bankWithZeroCapacity.getCapacity());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bankWithZeroCapacity.addClient(client1);
        });
    }

    @Test
    public void testClientIsApprovedForLoan(){
        Assertions.assertTrue(client1.isApprovedForLoan());
    }

    @Test
    public void testGetClientName() {
        Assertions.assertEquals("Client1", client1.getName());
    }

    @Test
    public void testAddSameClientTwice() {
        bank.addClient(client1);
        bank.addClient(client1);
        Assertions.assertEquals(2, bank.getCount());
    }

    @Test
    public void testRemoveNonExistingClientMessage() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bank.removeClient("Gosho");
        });
        Assertions.assertEquals("Client named Gosho does not exist!", exception.getMessage());
    }

    @Test
    public void testLoanWithdrawalNonExistingClientMessage() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bank.loanWithdrawal("Gosho");
        });
        Assertions.assertEquals("Client named Gosho does not exist!", exception.getMessage());
    }
}
