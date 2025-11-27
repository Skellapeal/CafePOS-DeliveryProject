package main.java.com.cafepos.app.events;

public record OrderItemAdded(long id) implements OrderEvent { }