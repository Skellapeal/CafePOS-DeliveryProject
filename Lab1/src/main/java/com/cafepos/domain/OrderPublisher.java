package main.java.com.cafepos.domain;

public interface OrderPublisher
{
    void registerOrder(OrderObserver o);
    void unregisterOrder(OrderObserver o);
    void notifyObservers(String event);
}
