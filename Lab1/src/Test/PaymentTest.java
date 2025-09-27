package Test;

import Main.Catalog.Catalog;
import Main.Catalog.InMemoryCatalog;
import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import Main.Domain.LineItem;
import Main.Domain.Order;
import Main.Domain.OrderIds;
import Main.Payment.CardPayment;
import Main.Payment.CashPayment;
import Main.Payment.PaymentStrategy;
import Main.Payment.WalletPayment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentTest
{
    @Test void payment_strategy_called()
    {
        var p = new SimpleProduct("A", "A", Money.of(5));
        var order = new Order(42);
        order.addItem(new LineItem(p, 1));
        final boolean[] called = {false};
        PaymentStrategy fake = _ -> called[0] = true;
        order.pay(fake);
        assertTrue(called[0], "Payment strategy should be called");
    }
    @Test
    public void testCardPayment()
    {
        //Initialisation of Order to test payment methode
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));

        //catching the print stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        order.pay(new CardPayment("123456789"));

        //restore original System.out
        System.setOut(originalOut);

        //System.lineSeparator is used as the void methode uses printLn()
        String expectedOutput = "[Card] Customer paid 5.50 EUR with card ****6789" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }
    @Test
    public void testWalletPayment()
    {
        //Initialisation of Order to test payment methode
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));

        //catching the print stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        order.pay(new WalletPayment("Neil&Jan-Joint-Financial-Account"));

        //restore original System.out
        System.setOut(originalOut);

        //System.lineSeparator is used as the void methode uses printLn()
        String expectedOutput = "[Wallet] Customer paid 5.50 EUR with wallet Neil&Jan-Joint-Financial-Account" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }
    @Test
    public void testCashPayment()
    {
        //Initialisation of Order to test payment methode
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));

        //catching the print stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        order.pay(new CashPayment());

        //restore original System.out
        System.setOut(originalOut);

        //System.lineSeparator is used as the void methode uses printLn()
        String expectedOutput = "[Cash] Customer paid 5.50 EUR" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }
}
