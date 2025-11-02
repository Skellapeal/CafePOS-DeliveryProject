package main.java.com.cafepos.test;

import main.java.com.cafepos.order.CustomerNotifier;
import main.java.com.cafepos.order.Order;
import main.java.com.cafepos.order.OrderIds;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerNotifierTest
{
    @Test
    public void testCustomerNotifierWithItemAddedEvent()
    {
        CustomerNotifier notifier = new CustomerNotifier();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        notifier.updated(order, "itemAdded");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("[Customer]"));
        assertTrue(output.contains("Dear customer"));
        assertTrue(output.contains(order.id()));
        assertTrue(output.contains("itemAdded"));
    }

    @Test
    public void testCustomerNotifierWithPaidEvent()
    {
        CustomerNotifier notifier = new CustomerNotifier();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        notifier.updated(order, "paid");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("[Customer]"));
        assertTrue(output.contains("paid"));
        assertTrue(output.contains(order.id()));
    }

    @Test
    public void testCustomerNotifierWithReadyEvent()
    {
        CustomerNotifier notifier = new CustomerNotifier();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        notifier.updated(order, "ready");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("[Customer]"));
        assertTrue(output.contains("ready"));
    }

    @Test
    public void testCustomerNotifierWithCustomEvent()
    {
        CustomerNotifier notifier = new CustomerNotifier();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        notifier.updated(order, "customEvent");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("customEvent"));
    }
}