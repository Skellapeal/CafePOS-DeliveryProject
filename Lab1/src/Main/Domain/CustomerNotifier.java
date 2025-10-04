package Main.Domain;

public final class CustomerNotifier implements OrderObserver
{
    @Override
    public void updated(Order order,String event)
    {
        System.out.printf("[Customer] Dear customer, your Order: %s has been updated: %s\n",order.id(),event);
    }
}
