package main.java.com.cafepos.commands;

import main.java.com.cafepos.catalog.Product;
import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.order.LineItem;

public class AddItemCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        if(args.length < 2)
        {
            context.displayError("Usage: add <recipe> <quantity>");
            System.out.println("Example: add LAT_SHOT_OAT 2");
            return;
        }

        String recipe = args[0].replace("_", " ");
        try
        {
            int quantity = Integer.parseInt(args[1]);
            addProductToOrder(recipe, quantity, context);
        }
        catch (NumberFormatException e)
        {
            context.displayError("Invalid quantity: " + args[1]);
        }
    }

    private void addProductToOrder(String recipe, int quantity, CommandContext context)
    {
        try
        {
            Product product = context.getProductFactory().create(recipe);
            context.getCurrentOrder().addItem(new LineItem(product, quantity));

            Money itemTotal = product.basePrice().multiply(quantity);
            System.out.println("\nAdded to order:");
            System.out.println(" " + product.name() + " x" + quantity + " - €" + itemTotal);
            System.out.println(" Order total: €" + context.getCurrentOrder().totalWithTax(24));
        }
        catch (IllegalArgumentException e)
        {
            context.displayError("Invalid Recipe: " + e.getMessage());
            System.out.println("\nUse 'menu' to see available options.");
        }
    }
}