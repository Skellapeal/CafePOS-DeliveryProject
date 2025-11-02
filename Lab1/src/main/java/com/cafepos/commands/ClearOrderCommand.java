package main.java.com.cafepos.commands;

import main.java.com.cafepos.order.*;

public class ClearOrderCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        System.out.print("Are you sure you want to clear the current order? (y/n): ");
        String confirm = context.getScanner().nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes"))
        {
            Order newOrder = new Order(OrderIds.next());

            newOrder.registerOrder(new KitchenDisplay());
            newOrder.registerOrder(new DeliveryDesk());
            newOrder.registerOrder(new CustomerNotifier());

            context.setCurrentOrder(newOrder);
            context.displaySuccess("Order cleared");
        }
    }
}