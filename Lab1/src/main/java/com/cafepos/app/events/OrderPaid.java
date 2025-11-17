package main.java.com.cafepos.app.events;

public record OrderPaid(long orderId) implements OrderEvent { }