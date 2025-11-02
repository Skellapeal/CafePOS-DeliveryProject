package main.java.com.cafepos.order;

public interface OrderPublisher
{
    void registerOrder(OrderObserver o);
    void unregisterOrder(OrderObserver o);
    void notifyObservers(String event);
}
