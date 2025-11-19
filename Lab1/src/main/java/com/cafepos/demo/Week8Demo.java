package main.java.com.cafepos.demo;

import main.java.com.cafepos.app.commands.AddItemCommand;
import main.java.com.cafepos.app.commands.OrderService;
import main.java.com.cafepos.app.commands.PayCommand;
import main.java.com.cafepos.app.commands.PosRemote;
import main.java.com.cafepos.domain.Order;
import main.java.com.cafepos.domain.OrderIds;
import main.java.com.cafepos.domain.payment.CardPayment;

public class Week8Demo
{
    public static void main(String[] args)
    {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);
        remote.setSlot(0, new AddItemCommand(service, "ESP SHOT OAT", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT L", 2));
        remote.setSlot(2, new PayCommand(service, new CardPayment("1234567890123456"), 10));
        remote.press(0);
        remote.press(1);
        remote.undo();
        remote.press(1);
        remote.press(2);
    }
}
