package main.java.com.cafepos.ui;

import main.java.com.cafepos.app.events.*;
import main.java.com.cafepos.infrastructure.Wiring;

public class EventWiringDemo
{
    public static void main(String[] args)
    {
        var bus = new EventsBus();
        var comp = Wiring.createDefault();
        var controller = new OrderController(comp.repo(), comp.checkout());

        bus.on(OrderCreated.class, event -> System.out.println("[UI] order created: " + event.orderId()));
        bus.on(OrderPaid.class, event -> System.out.println("[UI] order paid: " + event.orderId()));

        long id = 4201L;
        controller.createOrder(id);

        bus.emit(new OrderCreated(id));
        bus.emit(new OrderPaid(id));
    }
}
