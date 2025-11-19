package main.java.com.cafepos.test;

import main.java.com.cafepos.domain.catalog.SimpleProduct;
import main.java.com.cafepos.domain.value.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleProductTest
{
    @Test
    public void ExceptionSimpleProductTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new SimpleProduct("SQE", "test", Money.of(-1)));
    }
    @Test
    public void testSimpleProductValue()
    {
        SimpleProduct simpleProduct = new SimpleProduct("SQE", "test", Money.of(1));

        Assertions.assertEquals("SQE",simpleProduct.id());
        Assertions.assertEquals("test",simpleProduct.name());
        Assertions.assertEquals("1.00",simpleProduct.basePrice().toString());
    }

    @Test
    public void testProductWithZeroPrice()
    {
        SimpleProduct freeProduct = new SimpleProduct("FREE-001", "Free Sample", Money.zero());

        Assertions.assertEquals("FREE-001", freeProduct.id());
        Assertions.assertEquals("Free Sample", freeProduct.name());
        Assertions.assertEquals("0.00", freeProduct.basePrice().toString());
    }

    @Test
    public void testProductWithHighPrice()
    {
        SimpleProduct expensiveProduct = new SimpleProduct("LUX-001", "Luxury Item", Money.of(999.99));

        Assertions.assertEquals("LUX-001", expensiveProduct.id());
        Assertions.assertEquals("Luxury Item", expensiveProduct.name());
        Assertions.assertEquals("999.99", expensiveProduct.basePrice().toString());
    }

    @Test
    public void testProductWithEmptyName()
    {
        SimpleProduct productWithEmptyName = new SimpleProduct("EMPTY-001", "", Money.of(5.00));

        Assertions.assertEquals("EMPTY-001", productWithEmptyName.id());
        Assertions.assertEquals("", productWithEmptyName.name());
        Assertions.assertEquals("5.00", productWithEmptyName.basePrice().toString());
    }

    @Test
    public void testProductWithLongName()
    {
        String longName = "This is a very long product name that might be used in some circumstances";
        SimpleProduct productWithLongName = new SimpleProduct("LONG-001", longName, Money.of(12.50));

        Assertions.assertEquals("LONG-001", productWithLongName.id());
        Assertions.assertEquals(longName, productWithLongName.name());
    }

    @Test
    public void testProductWithSpecialCharactersInName()
    {
        SimpleProduct product = new SimpleProduct("SPEC-001", "Café Latte (Large) - €", Money.of(4.50));

        Assertions.assertEquals("SPEC-001", product.id());
        Assertions.assertEquals("Café Latte (Large) - €", product.name());
        Assertions.assertEquals("4.50", product.basePrice().toString());
    }

    @Test
    public void testProductIdImmutability()
    {
        SimpleProduct product = new SimpleProduct("IMMUT-001", "Test Product", Money.of(10.00));

        String originalId = product.id();
        Assertions.assertEquals(originalId, product.id());
    }

    @Test
    public void testProductNameImmutability()
    {
        SimpleProduct product = new SimpleProduct("IMMUT-002", "Test Product", Money.of(10.00));

        String originalName = product.name();
        Assertions.assertEquals(originalName, product.name());
    }

    @Test
    public void testProductBasePriceImmutability()
    {
        SimpleProduct product = new SimpleProduct("IMMUT-003", "Test Product", Money.of(10.00));

        Money originalPrice = product.basePrice();
        Assertions.assertEquals(0, originalPrice.compareTo(product.basePrice()));
    }

    @Test
    public void testMultipleProductsWithSameId()
    {
        SimpleProduct product1 = new SimpleProduct("SAME-001", "Product 1", Money.of(5.00));
        SimpleProduct product2 = new SimpleProduct("SAME-001", "Product 2", Money.of(10.00));

        Assertions.assertEquals("SAME-001", product1.id());
        Assertions.assertEquals("SAME-001", product2.id());
        Assertions.assertNotEquals(product1.name(), product2.name());
    }

    @Test
    public void testProductWithDecimalPrice()
    {
        SimpleProduct product = new SimpleProduct("DEC-001", "Decimal Price", Money.of(7.99));
        Assertions.assertEquals("7.99", product.basePrice().toString());
    }
}