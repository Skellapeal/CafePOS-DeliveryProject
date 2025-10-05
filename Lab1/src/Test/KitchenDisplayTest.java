package Test;

import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import Main.Domain.KitchenDisplay;
import Main.Domain.LineItem;
import Main.Domain.Order;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import Main.Domain.OrderIds;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class KitchenDisplayTest
{
    @Test
    public void testKitchenDisplayItemAddedEvent()
    {
        KitchenDisplay kitchenDisplay = new KitchenDisplay();
        Order order = new Order(OrderIds.next());
        SimpleProduct product = new SimpleProduct("P-KITCHEN", "Kitchen Test", Money.of(5.00));
        order.addItem(new LineItem(product, 2));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        kitchenDisplay.updated(order, "itemAdded");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("[Kitchen]"));
        assertTrue(output.contains(order.id()));
        assertTrue(output.contains("2"));
        assertTrue(output.contains("Kitchen Test"));
    }

    @Test
    public void testKitchenDisplayPaidEvent()
    {
        KitchenDisplay kitchenDisplay = new KitchenDisplay();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        kitchenDisplay.updated(order, "paid");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("[Kitchen]"));
        assertTrue(output.contains("Payment Received"));
        assertTrue(output.contains(order.id()));
    }

    @Test
    public void testKitchenDisplayIgnoresOtherEvents()
    {
        KitchenDisplay kitchenDisplay = new KitchenDisplay();
        Order order = new Order(OrderIds.next());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        kitchenDisplay.updated(order, "ready");
        kitchenDisplay.updated(order, "cancelled");
        kitchenDisplay.updated(order, "unknown");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.isEmpty());
    }
}