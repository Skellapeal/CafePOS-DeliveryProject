package main.java.com.cafepos.test;

import main.java.com.cafepos.order.Order;
import main.java.com.cafepos.order.OrderIds;
import main.java.com.cafepos.order.OrderObserver;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderObserverTest
{
    @Test
    public void testObserverImplementation()
    {
        TestOrderObserver observer = new TestOrderObserver();
        Order order = new Order(OrderIds.next());

        observer.updated(order, "main.java.com.cafepos.test-event");

        assertEquals(1, observer.updateCount);
        assertEquals("main.java.com.cafepos.test-event", observer.lastEvent);
        assertNotNull(observer.lastOrder);
        assertEquals(order.id(), observer.lastOrder.id());
    }

    @Test
    public void testObserverWithMultipleEvents()
    {
        TestOrderObserver observer = new TestOrderObserver();
        Order order = new Order(OrderIds.next());

        observer.updated(order, "event1");
        observer.updated(order, "event2");
        observer.updated(order, "event3");

        assertEquals(3, observer.updateCount);
        assertEquals("event3", observer.lastEvent);
    }

    @Test
    public void testObserverWithDifferentOrders()
    {
        TestOrderObserver observer = new TestOrderObserver();
        Order order1 = new Order(OrderIds.next());
        Order order2 = new Order(OrderIds.next());

        observer.updated(order1, "order1-event");
        observer.updated(order2, "order2-event");

        assertEquals(2, observer.updateCount);
        assertEquals("order2-event", observer.lastEvent);
        assertEquals(order2.id(), observer.lastOrder.id());
    }

    @Test
    public void testObserverWithNullEvent()
    {
        TestOrderObserver observer = new TestOrderObserver();
        Order order = new Order(OrderIds.next());

        observer.updated(order, null);

        assertEquals(1, observer.updateCount);
        assertNull(observer.lastEvent);
    }

    @Test
    public void testObserverWithEmptyEvent()
    {
        TestOrderObserver observer = new TestOrderObserver();
        Order order = new Order(OrderIds.next());

        observer.updated(order, "");

        assertEquals(1, observer.updateCount);
        assertEquals("", observer.lastEvent);
    }

    private static class TestOrderObserver implements OrderObserver
    {
        public int updateCount = 0;
        public String lastEvent = null;
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