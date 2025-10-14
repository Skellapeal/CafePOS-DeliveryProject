package Main.Commands;

import Main.Catalog.Product;
import Main.Common.Money;
import Main.Domain.LineItem;
import Main.Domain.Priced;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        String base = promptForBase(context.getScanner());
        if(base == null) return;

        List<String> addons = promptForAddons(context.getScanner());

        int quantity = promptForQuantity(context.getScanner(), context);
        if(quantity <= 0) return;

        String recipe = buildRecipe(base, addons);
        if(confirmOrder(recipe, quantity, context))
        {
            addProductToOrder(recipe, quantity, context);
        }
    }

    private String promptForBase(Scanner scanner)
    {
        System.out.print("Step 1: Choose a product");
        System.out.println("Available options:");
        System.out.println("   1. ESP - Espresso (€2.50)");
        System.out.println("   2. LAT - Latte (€3.20)");
        System.out.println("   3. CAP - Cappuccino (€3.00)");
        System.out.print("\nEnter choice (1-3, EPS/LAT/CAP, or 'cancel'): ");

        String input = scanner.nextLine().trim().toUpperCase();

        return switch(input)
        {
            case "1", "ESP" -> "ESP";
            case "2", "LAT" -> "LAT";
            case "3", "CAP" -> "CAP";
            case "CANCEL" ->
            {
                System.out.println("Order cancelled.");
                yield null;
            }
            default ->
            {
                System.out.println("❌ Invalid selection. Order cancelled.");
                yield null;
            }
        };
    }

    private List<String> promptForAddons(Scanner scanner)
    {
        System.out.println("Step 2: Add customisations (optional)");
        System.out.println("Available options:");
        System.out.println("   1. SHOT - Extra shot (+€0.80)");
        System.out.println("   2. OAT - Oat Milk (+€0.30");
        System.out.println("   3. SYP - Flavoured Syrup (+€0.40)");
        System.out.println("   4. L - Large(+€0.70)");

        List<String> addons = new ArrayList<>();

        while(true)
        {
            System.out.print("\nAdd customization (1-4, SHOT/OAT/SYP/L, or 'done'): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if(input.equals("DONE") || input.isEmpty())
            {
                break;
            }

            String addon = switch(input)
            {
                case "1", "SHOT" -> "SHOT";
                case "2", "OAT" -> "OAT";
                case "3", "SYP" -> "SYP";
                case "4", "L" -> "L";
                default -> null;
            };

            if(addon != null &&  !addons.contains(addon))
            {
                addons.add(addon);
                System.out.println("Added: " + addon);
            }
            else if(addon != null)
            {
                System.out.println("Already added: " + addon);
            }
            else
            {
                System.out.println("Invalid option: " + input);
            }
        }
        return addons;
    }

    private int promptForQuantity(Scanner scanner,CommandContext context)
    {
        System.out.println("Step 3: How many would you like?");
        try
        {
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            if(quantity > 0) return quantity;
            else
            {
                System.out.println("Quantity must be greater than 0.");
                return 0;
            }
        }
        catch(NumberFormatException e)
        {
            context.displayError("Invalid quantity.");
            return 0;
        }
    }

    private String buildRecipe(String base, List<String> addons)
    {
        return base + (addons.isEmpty() ? "" : " " + String.join(" ", addons));
    }

    private boolean confirmOrder(String recipe, int quantity, CommandContext context)
    {
        try
        {
            Product product = context.getProductFactory().create(recipe);
            Money total;
            if(product instanceof Priced priced)
            {
                total = priced.price().multiply(quantity);
            }
            else
            {
                total = product.basePrice().multiply(quantity);
            }

            String divider = "-------------------------------------------------------";
            System.out.println("\n" + divider);
            System.out.println("Order Summary:");
            System.out.println("   Product: " + product.name());
            System.out.println("   Recipe:  " + recipe);
            System.out.println("   Quantity: " + quantity);
            System.out.println("   Total:   €" + total);
            System.out.println(divider);

            System.out.print("Confirm order? (y/n): ");
            String confirm = context.getScanner().nextLine().trim().toLowerCase();

            return confirm.equals("y") || confirm.equals("yes");
        }
        catch (IllegalArgumentException e)
        {
            context.displayError("Invalid recipe: " + e.getMessage());
            return false;
        }
    }

    private void addProductToOrder(String recipe, int quantity, CommandContext context)
    {
        try
        {
            Product product = context.getProductFactory().create(recipe);
            context.getCurrentOrder().addItem(new LineItem(product, quantity));

            Money total;
            if(product instanceof Priced priced)
            {
                total = priced.price().multiply(quantity);
            }
            else
            {
                total = product.basePrice().multiply(quantity);
            }
            System.out.println("\nAdded to order:");
            System.out.println("   " + product.name() + " x" + quantity + " - €" + total);
            System.out.println("   Order total (@24%): €" + context.getCurrentOrder().totalWithTax(24));
        }
        catch (IllegalArgumentException e)
        {
            context.displayError("Invalid recipe: " + e.getMessage());
            System.out.println("\nUse 'menu' to see available options");
        }
    }
}