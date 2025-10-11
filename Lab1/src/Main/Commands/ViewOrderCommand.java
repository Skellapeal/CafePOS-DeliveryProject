package Main.Commands;

import Main.Domain.LineItem;

public class ViewOrderCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        context.displaySection("Current Order #" + context.getCurrentOrder().id());

        if (context.getCurrentOrder().items().isEmpty())
        {
            System.out.println("Your order is empty");
            System.out.println("\nUse 'order' to start adding items!");
            return;
        }

        System.out.println("Items:");
        for (int i = 0; i < context.getCurrentOrder().items().size(); i++)
        {
            LineItem item = context.getCurrentOrder().items().get(i);
            System.out.printf("   %d. %s x%d - €%s%n",
                    i + 1,
                    item.product().name(),
                    item.quantity(),
                    item.lineTotal());
        }

        String divider = "-------------------------------------------------------";
        System.out.println(divider);
        System.out.printf("Subtotal: €%s%n", context.getCurrentOrder().subtotal());
        System.out.printf("Tax (24%%): €%s%n", context.getCurrentOrder().taxAtPercent(24));
        System.out.printf("Total: €%s%n", context.getCurrentOrder().totalWithTax(24));
    }
}