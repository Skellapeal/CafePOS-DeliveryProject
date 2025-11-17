package main.java.com.cafepos.app.events;

public record OrderCreated(long orderId) implements OrderEvent { }
