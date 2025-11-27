package main.java.com.cafepos.app.events;

public sealed interface OrderEvent permits OrderCreated, OrderItemAdded, OrderPaid, OrderReady { }
