package main.java.com.cafepos.test;

import main.java.com.cafepos.catalog.Product;
import main.java.com.cafepos.catalog.SimpleProduct;
import main.java.com.cafepos.domain.Money;
import main.java.com.cafepos.decorator.ExtraShot;
import main.java.com.cafepos.decorator.OatMilk;
import main.java.com.cafepos.decorator.SizeLarge;
import main.java.com.cafepos.domain.LineItem;
import main.java.com.cafepos.domain.Order;
import main.java.com.cafepos.decorator.Priced;
import main.java.com.cafepos.factory.ProductFactory;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest
{
    @Test
    void decorator_single_addon()
    {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso);

        Assertions.assertEquals("Espresso + Extra Shot", withShot.name());

        // if using Priced interface:
        assertEquals(Money.of(3.30).toString(), ((Priced) withShot).price().toString());
    }

    @Test
    void decorator_stacks()
    {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));

        assertEquals("Espresso + Extra Shot + Oat Milk (Large)", decorated.name());
        Assertions.assertEquals(Money.of(4.50).toString(), ((Priced) decorated).price().toString());
    }

    @Test
    void factory_parses_recipe()
    {
        ProductFactory f = new ProductFactory();
        Product p = f.create("ESP SHOT OAT");

        assertTrue(p.name().contains("Espresso") && p.name().contains("Oat Milk"));
    }

    @Test
    void order_uses_decorated_price()
    {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso); // 3.30

        Order o = new Order(1);
        o.addItem(new LineItem(withShot, 2));

        Assertions.assertEquals(Money.of(6.60).toString(), o.subtotal().toString());
    }

    @Test
    void order_uses_decorated_unknown()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ProductFactory f = new ProductFactory();
            Product p = f.create("ESP UNKNOWN");
        });
    }

    @Test
    void order_uses_product_unknown()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ProductFactory f = new ProductFactory();
            Product p = f.create("RIP L");
        });
    }
}
