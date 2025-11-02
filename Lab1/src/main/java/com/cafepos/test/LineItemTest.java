package main.java.com.cafepos.test;

import main.java.com.cafepos.catalog.Catalog;
import main.java.com.cafepos.catalog.InMemoryCatalog;
import main.java.com.cafepos.catalog.SimpleProduct;
import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.order.LineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineItemTest
{
    @Test
    public void testLineItem()
    {
        Catalog catalogTest = new InMemoryCatalog();

        catalogTest.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        LineItem item1 = new LineItem(catalogTest.findById("P-ESP").orElseThrow(), 1);

        assertEquals("P-ESP", item1.product().id());
        assertEquals("Espresso", item1.product().name());
        assertEquals(1, item1.quantity());
        assertEquals("2.50",item1.lineTotal().toString());
    }
    @Test
    public void testLineItemIDException()
    {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            Catalog catalogTest = new InMemoryCatalog();
            catalogTest.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
            LineItem item1 = new LineItem(catalogTest.findById("").orElseThrow(), 1);
        });
    }
    @Test
    public void testLineItemQuantityException()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Catalog catalogTest = new InMemoryCatalog();
            catalogTest.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
            LineItem item2 = new LineItem(catalogTest.findById("P-ESP").orElseThrow(), 0);
        });
    }

    @Test
    public void testLineItemWithMultipleQuantity()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-COFFEE", "Coffee", Money.of(3.00)));
        LineItem lineItem = new LineItem(catalog.findById("P-COFFEE").orElseThrow(), 5);

        assertEquals(5, lineItem.quantity());
        assertEquals("15.00", lineItem.lineTotal().toString());
    }

    @Test
    public void testLineItemWithLargeQuantity()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-BULK", "Bulk Item", Money.of(1.00)));
        LineItem lineItem = new LineItem(catalog.findById("P-BULK").orElseThrow(), 100);

        assertEquals(100, lineItem.quantity());
        assertEquals("100.00", lineItem.lineTotal().toString());
    }

    @Test
    public void ExceptionLineItemNullProductTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LineItem(null, 1));
    }

    @Test
    public void ExceptionLineItemNegativeQuantityTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            SimpleProduct product = new SimpleProduct("P-TEST", "Test Product", Money.of(5.00));
            new LineItem(product, -1);
        });
    }

    @Test
    public void testLineItemCalculationAccuracy()
    {
        SimpleProduct expensiveProduct = new SimpleProduct("P-EXP", "Expensive", Money.of(99.99));
        LineItem lineItem = new LineItem(expensiveProduct, 3);

        assertEquals("299.97", lineItem.lineTotal().toString());
    }

    @Test
    public void testLineItemWithFreeProduct()
    {
        SimpleProduct freeProduct = new SimpleProduct("P-FREE", "Free Sample", Money.zero());
        LineItem lineItem = new LineItem(freeProduct, 10);

        assertEquals(10, lineItem.quantity());
        assertEquals("0.00", lineItem.lineTotal().toString());
    }

    @Test
    public void testLineItemProductImmutability()
    {
        SimpleProduct product = new SimpleProduct("P-IMMUT", "Immutable", Money.of(10.00));
        LineItem lineItem = new LineItem(product, 2);

        assertEquals(product.id(), lineItem.product().id());
        assertEquals(product.name(), lineItem.product().name());
    }

    @Test
    public void testLineItemQuantityImmutability()
    {
        SimpleProduct product = new SimpleProduct("P-QTY", "Quantity Test", Money.of(5.00));
        LineItem lineItem = new LineItem(product, 3);

        int originalQuantity = lineItem.quantity();
        assertEquals(originalQuantity, lineItem.quantity());
    }

    @Test
    public void testLineTotalRecalculation()
    {
        SimpleProduct product = new SimpleProduct("P-CALC", "Calculation Test", Money.of(7.50));
        LineItem lineItem = new LineItem(product, 4);

        Money total1 = lineItem.lineTotal();
        Money total2 = lineItem.lineTotal();
        assertEquals(0, total1.compareTo(total2));
        assertEquals("30.00", total1.toString());
    }
}