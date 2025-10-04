package Main.Domain;

public interface OrderObserver
{
    void updated(Order order, String event);
}
