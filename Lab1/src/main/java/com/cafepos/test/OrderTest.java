package main.java.com.cafepos.test;

import main.java.com.cafepos.catalog.Catalog;
import main.java.com.cafepos.catalog.InMemoryCatalog;
import main.java.com.cafepos.catalog.SimpleProduct;
import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.domain.LineItem;
import main.java.com.cafepos.domain.Order;
import main.java.com.cafepos.domain.OrderIds;
import main.java.com.cafepos.domain.OrderObserver;
import main.java.com.cafepos.payment.CashPayment;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest
{
    @BeforeEach
    public void resetOrderIds() throws Exception
    {
        Field field = OrderIds.class.getDeclaredField("sequence");
        field.setAccessible(true);
        field.setLong(null, 1001L);
    }

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

        assertEquals(0, subtotal.compareTo(Money.of(8.50)));

        assertEquals(2, order.items().size());

        assertEquals(0, taxAtPercent.compareTo(Money.of(0.85)));

        assertEquals(0, totalWithTax.compareTo(Money.of(9.35)));

        assertEquals("1001", order.id());
    }

    @Test
    public void testOrderWithAtTaxPercentException()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(OrderIds.next());
            order.taxAtPercent(-1);
        });
    }

    @Test
    public void testOrderWithTotalWithTaxException()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(OrderIds.next());
            order.totalWithTax(-1);
        });
    }

    @Test
    public void testEmptyOrderSubtotal()
    {
        Order emptyOrder = new Order(OrderIds.next());
        assertEquals(0, emptyOrder.subtotal().compareTo(Money.zero()));
    }

    @Test
    public void testEmptyOrderTaxCalculation()
    {
        Order emptyOrder = new Order(OrderIds.next());
        assertEquals(0, emptyOrder.taxAtPercent(15).compareTo(Money.zero()));
        assertEquals(0, emptyOrder.totalWithTax(15).compareTo(Money.zero()));
    }

    @Test
    public void testOrderWithSingleItem()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-SINGLE", "Single Item", Money.of(10.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-SINGLE").orElseThrow(), 1));

        assertEquals(1, order.items().size());
        assertEquals(0, order.subtotal().compareTo(Money.of(10.00)));
        assertEquals(0, order.taxAtPercent(20).compareTo(Money.of(2.00)));
        assertEquals(0, order.totalWithTax(20).compareTo(Money.of(12.00)));
    }

    @Test
    public void testOrderWithMultipleItemsSameProduct()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-MULTI", "Multi Item", Money.of(5.00)));
        Order order = new Order(OrderIds.next());

        order.addItem(new LineItem(catalog.findById("P-MULTI").orElseThrow(), 2));
        order.addItem(new LineItem(catalog.findById("P-MULTI").orElseThrow(), 3));

        assertEquals(2, order.items().size());
        assertEquals(0, order.subtotal().compareTo(Money.of(25.00)));
    }

    @Test
    public void testOrderObserverRegistration()
    {
        Order order = new Order(OrderIds.next());
        TestObserver observer = new TestObserver();

        order.registerOrder(observer);
        assertEquals(0, observer.updateCount);

        SimpleProduct product = new SimpleProduct("P-OBS", "Observer Test", Money.of(1.00));
        order.addItem(new LineItem(product, 1));
        assertEquals(1, observer.updateCount);
        assertEquals("itemAdded", observer.lastEvent);
    }

    @Test
    public void testOrderObserverUnregistration()
    {
        Order order = new Order(OrderIds.next());
        TestObserver observer = new TestObserver();

        order.registerOrder(observer);
        order.unregisterOrder(observer);

        SimpleProduct product = new SimpleProduct("P-UNREG", "Unregister Test", Money.of(1.00));
        order.addItem(new LineItem(product, 1));
        assertEquals(0, observer.updateCount);
    }

    @Test
    public void ExceptionOrderObserverNullRegistrationTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(OrderIds.next());
            order.registerOrder(null);
        });
    }

    @Test
    public void ExceptionOrderObserverNullUnregistrationTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(OrderIds.next());
            order.unregisterOrder(null);
        });
    }

    @Test
    public void testOrderPaymentNotification()
    {
        Order order = new Order(OrderIds.next());
        TestObserver observer = new TestObserver();
        order.registerOrder(observer);

        SimpleProduct product = new SimpleProduct("P-PAY", "Payment Test", Money.of(10.00));
        order.addItem(new LineItem(product, 1));

        order.pay(new CashPayment());
        assertEquals(2, observer.updateCount);
        assertEquals("paid", observer.lastEvent);
    }

    @Test
    public void testOrderMarkReady()
    {
        Order order = new Order(OrderIds.next());
        TestObserver observer = new TestObserver();
        order.registerOrder(observer);

        order.markReady();
        assertEquals(1, observer.updateCount);
        assertEquals("ready", observer.lastEvent);
    }

    @Test
    public void ExceptionOrderPaymentNullTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(OrderIds.next());
            order.pay(null);
        });
    }

    @Test
    public void testOrderIdConsistency()
    {
        long orderId = OrderIds.next();
        Order order = new Order(orderId);
        assertEquals(String.valueOf(orderId), order.id());
    }

    @Test
    public void testOrderWithZeroPercentTax()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-NOTAX", "No Tax Item", Money.of(20.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-NOTAX").orElseThrow(), 1));

        assertEquals(0, order.taxAtPercent(0).compareTo(Money.zero()));
        assertEquals(0, order.totalWithTax(0).compareTo(Money.of(20.00)));
    }

    @Test
    public void testOrderWithHighTaxPercent()
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-HITAX", "High Tax Item", Money.of(100.00)));
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-HITAX").orElseThrow(), 1));

        assertEquals(0, order.taxAtPercent(50).compareTo(Money.of(50.00)));
        assertEquals(0, order.totalWithTax(50).compareTo(Money.of(150.00)));
    }

    private static class TestObserver implements OrderObserver
    {
        public int updateCount = 0;
        public String lastEvent = "";
        public Order lastOrder = null;

        @Override
        public void updated(Order order, String event)
        {
            updateCount++;
            lastEvent = event;
            lastOrder = order;
        }
    }
}