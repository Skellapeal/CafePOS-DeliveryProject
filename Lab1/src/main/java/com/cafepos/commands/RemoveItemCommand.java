package main.java.com.cafepos.commands;

import main.java.com.cafepos.order.LineItem;

public class RemoveItemCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        if (context.getCurrentOrder().items().isEmpty())
        {
            context.displayError("Order is empty");
            return;
        }

        new ViewOrderCommand().execute(new String[0], context);

        System.out.print("\nEnter item number to remove (1-" + context.getCurrentOrder().items().size() + "): ");
        try
        {
            int index = Integer.parseInt(context.getScanner().nextLine().trim()) - 1;
            if (index >= 0 && index < context.getCurrentOrder().items().size())
            {
                LineItem removed = context.getCurrentOrder().items().remove(index);
                context.displaySuccess("Removed: " + removed.product().name());
            }
            else
            {
                context.displayError("Invalid item number");
            }
        }
        catch (NumberFormatException e)
        {
            context.displayError("Invalid number");
        }
    }
}