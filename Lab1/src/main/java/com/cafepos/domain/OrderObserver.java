package main.java.com.cafepos.domain;

public interface OrderObserver
{
    void updated(Order order, String event);
}
