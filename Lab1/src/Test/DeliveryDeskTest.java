package Test;

import Main.Domain.DeliveryDesk;
import Main.Domain.Order;
import Main.Domain.OrderIds;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeliveryDeskTest
{
    @Test
    public void testDeliveryDeskReadyEvent()
    {
        DeliveryDesk deliveryDesk = new DeliveryDesk();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        deliveryDesk.updated(order, "ready");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("[Delivery]"));
        assertTrue(output.contains("ready for delivery"));
        assertTrue(output.contains(order.id()));
    }

    @Test
    public void testDeliveryDeskIgnoresOtherEvents()
    {
        DeliveryDesk deliveryDesk = new DeliveryDesk();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        deliveryDesk.updated(order, "itemAdded");
        deliveryDesk.updated(order, "paid");
        deliveryDesk.updated(order, "cancelled");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.isEmpty());
    }
}