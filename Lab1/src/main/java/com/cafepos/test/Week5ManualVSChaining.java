package main.java.com.cafepos.test;

import main.java.com.cafepos.domain.catalog.Product;
import main.java.com.cafepos.domain.catalog.SimpleProduct;
import main.java.com.cafepos.domain.value.Money;
import main.java.com.cafepos.domain.decorator.ExtraShot;
import main.java.com.cafepos.domain.decorator.OatMilk;
import main.java.com.cafepos.domain.decorator.SizeLarge;
import main.java.com.cafepos.domain.LineItem;
import main.java.com.cafepos.domain.Order;
import main.java.com.cafepos.domain.decorator.Priced;
import main.java.com.cafepos.app.factory.ProductFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Week5ManualVSChaining
{

        @Test
        void factory_and_manual_build_same_drink() {
            // Build the decorated drink via factory
            Product viaFactory = new ProductFactory().create("espresso SHOT OAT L");

            // Build the same drink manually
            Product viaManual = new SizeLarge(new OatMilk(new ExtraShot(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)))));

            // Verify both names are identical
            assertEquals(viaManual.name(), viaFactory.name());

            // Verify both unit prices are identical (using Priced interface)
            Money factoryPrice = ((Priced) viaFactory).price();
            Money manualPrice = ((Priced) viaManual).price();

            assertEquals(manualPrice.toString(), factoryPrice.toString());

            // Verify both drinks behave identically in orders
            Order orderFromFactory = new Order(1);
            orderFromFactory.addItem(new LineItem(viaFactory, 1));

            Order orderFromManual = new Order(2);
            orderFromManual.addItem(new LineItem(viaManual, 1));

            // Compare subtotals
            assertEquals(orderFromManual.subtotal().toString(), orderFromFactory.subtotal().toString());

            // Compare totals with tax
            assertEquals(orderFromManual.totalWithTax(10).toString(), orderFromFactory.totalWithTax(10).toString());

    }

}
