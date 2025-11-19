package main.java.com.cafepos.infrastructure;

import main.java.com.cafepos.domain.Order;
import main.java.com.cafepos.domain.OrderObserver;

public final class KitchenDisplay implements OrderObserver
{
    @Override
    public void updated(Order order, String event)
    {
        if(event.equals("itemAdded"))
        {
            System.out.printf("\n[Kitchen] Order #%s: %s %s added\n", order.id(), order.items().getLast().quantity(), order.items().getLast().product().name());
        }
        if(event.equals("paid"))
        {
            System.out.printf("[Kitchen] Order #%s: Payment Received\n",order.id());
        }
    }
}
