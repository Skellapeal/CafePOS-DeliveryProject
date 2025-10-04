package Main.Domain;

public interface OrderPublisher
{
    void registerOrder(OrderObserver o);
    void unregisterOrder(OrderObserver o);
    void notifyObservers(Order order,  String event);
}
