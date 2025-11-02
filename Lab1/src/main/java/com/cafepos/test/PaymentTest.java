package main.java.com.cafepos.test;

import main.java.com.cafepos.catalog.Catalog;
import main.java.com.cafepos.catalog.InMemoryCatalog;
import main.java.com.cafepos.catalog.SimpleProduct;
import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.order.LineItem;
import main.java.com.cafepos.order.Order;
import main.java.com.cafepos.order.OrderIds;
import main.java.com.cafepos.payment.CardPayment;
import main.java.com.cafepos.payment.CashPayment;
import main.java.com.cafepos.payment.PaymentStrategy;
import main.java.com.cafepos.payment.WalletPayment;
import org.junit.jupiter.api.Test;
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
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        order.pay(new CardPayment("123456789"));

        System.setOut(originalOut);

        String expectedOutput = "[Card] Customer paid 5.50 EUR with card ****6789" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }
    @Test
    public void testWalletPayment()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        order.pay(new WalletPayment("Neil&Jan-Joint-Financial-Account"));

        System.setOut(originalOut);

        String expectedOutput = "[Wallet] Customer paid 5.50 EUR with wallet Neil&Jan-Joint-Financial-Account" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }
    @Test
    public void testCashPayment()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        order.pay(new CashPayment());

        System.setOut(originalOut);

        String expectedOutput = "[Cash] Customer paid 5.50 EUR" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCardPaymentWithShortCardNumber()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-SHORT", "Short Card Test", Money.of(10.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-SHORT").orElseThrow(), 1));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        order.pay(new CardPayment("1234"));
        System.setOut(originalOut);

        String expectedOutput = "[Card] Customer paid 11.00 EUR with card ****1234" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCardPaymentWithLongCardNumber()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-LONG", "Long Card Test", Money.of(15.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-LONG").orElseThrow(), 1));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        order.pay(new CardPayment("4111111111111111"));
        System.setOut(originalOut);

        String expectedOutput = "[Card] Customer paid 16.50 EUR with card ****1111" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testWalletPaymentWithEmptyWalletId()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-EMPTY", "Empty Wallet Test", Money.of(5.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-EMPTY").orElseThrow(), 1));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        order.pay(new WalletPayment(""));
        System.setOut(originalOut);

        String expectedOutput = "[Wallet] Customer paid 5.50 EUR with wallet " + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testWalletPaymentWithSpecialCharacters()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-SPECIAL", "Special Wallet Test", Money.of(8.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-SPECIAL").orElseThrow(), 1));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        order.pay(new WalletPayment("user@example.com"));
        System.setOut(originalOut);

        String expectedOutput = "[Wallet] Customer paid 8.80 EUR with wallet user@example.com" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCashPaymentWithZeroAmount()
    {
        Order emptyOrder = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        emptyOrder.pay(new CashPayment());
        System.setOut(originalOut);

        String expectedOutput = "[Cash] Customer paid 0.00 EUR" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCashPaymentWithLargeAmount()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-LARGE", "Large Amount Test", Money.of(500.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-LARGE").orElseThrow(), 2));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        order.pay(new CashPayment());
        System.setOut(originalOut);

        String expectedOutput = "[Cash] Customer paid 1100.00 EUR" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testMultiplePaymentStrategiesOnSameOrder()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-MULTI", "Multi Payment Test", Money.of(20.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-MULTI").orElseThrow(), 1));

        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        System.setOut(new PrintStream(outputStream1));
        order.pay(new CashPayment());
        System.setOut(originalOut);

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream2));
        order.pay(new CardPayment("987654321"));
        System.setOut(originalOut);

        assertTrue(outputStream1.toString().contains("[Cash]"));
        assertTrue(outputStream2.toString().contains("[Card]"));
    }

    @Test
    public void testPaymentStrategyImplementation()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-CUSTOM", "Custom Payment Test", Money.of(12.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-CUSTOM").orElseThrow(), 1));

        final StringBuilder paymentLog = new StringBuilder();
        PaymentStrategy customStrategy = (orderToPayFor) -> {
            paymentLog.append("Custom payment of ").append(orderToPayFor.totalWithTax(10));
        };

        order.pay(customStrategy);
        assertTrue(paymentLog.toString().contains("Custom payment of 13.20"));
    }

    @Test
    public void testPaymentOrderReference()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-REF", "Reference Test", Money.of(7.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-REF").orElseThrow(), 1));

        final Order[] receivedOrder = {null};
        PaymentStrategy testStrategy = (orderRef) -> receivedOrder[0] = orderRef;

        order.pay(testStrategy);
        assertEquals(order.id(), receivedOrder[0].id());
    }
}