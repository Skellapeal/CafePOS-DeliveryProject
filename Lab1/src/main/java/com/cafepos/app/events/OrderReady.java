package main.java.com.cafepos.app.events;

public record OrderReady(long id) implements OrderEvent { }