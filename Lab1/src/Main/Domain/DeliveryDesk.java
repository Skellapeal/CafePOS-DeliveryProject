package Main.Domain;

public final class DeliveryDesk implements OrderObserver
{
    @Override
    public void updated(Order order,String event)
    {
        if(event.equals("ready"))
        {
            System.out.printf("[Delivery] Order #%s: is ready for delivery\n",order.id());
        }
    }
}
