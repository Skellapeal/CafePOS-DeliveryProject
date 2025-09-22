package Test;

import Main.Catalog.Catalog;
import Main.Catalog.InMemoryCatalog;
import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import Main.Domain.LineItem;
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
}
