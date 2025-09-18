package Test;

import Main.Catalog.Catalog;
import Main.Catalog.InMemoryCatalog;
import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import Main.Domain.LineItem;
import Main.Domain.Order;
import Main.Domain.OrderIds;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class OrderTest

{
    @Test
    public void testOrder()
    {
        Catalog catalogTest = new InMemoryCatalog();

        catalogTest.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        catalogTest.add(new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50)));

        Order order = new Order(OrderIds.next());

        assertEquals(0,order.items().size());

        order.addItem(new LineItem(catalogTest.findById("P-ESP").orElseThrow(), 2));
        order.addItem(new LineItem(catalogTest.findById("P-CCK").orElseThrow(), 1));

        var subtotal = order.subtotal();
        var taxAtPercent = order.taxAtPercent(10);
        var totalWithTax = order.totalWithTax(10);

        //test if total order is calculated correctly
        assertEquals(0, subtotal.compareTo(Money.of(8.50)));

        assertEquals(2, order.items().size());

        assertEquals(0, taxAtPercent.compareTo(Money.of(0.85)));

        assertEquals(0, totalWithTax.compareTo(Money.of(9.35)));

        assertEquals("1001", order.id());
    }
    @Test
    public void testOrderWithAtTaxPercentException()
    {
        try
        {
            Order order = new Order(OrderIds.next());

            order.taxAtPercent(-1);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
    @Test
    public void testOrderWithTotalWithTaxException()
    {
        try
        {
            Order order = new Order(OrderIds.next());

            order.totalWithTax(-1);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}
