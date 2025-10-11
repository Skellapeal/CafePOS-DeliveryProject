package Main.Commands;

import Main.Catalog.Catalog;
import Main.Domain.Order;
import Main.Factory.ProductFactory;

import java.util.Scanner;

public class CommandContext
{
    private final Scanner scanner;
    private final ProductFactory productFactory;
    private final Catalog catalog;
    private Order currentOrder;

    public CommandContext(Scanner scanner, Catalog catalog, ProductFactory productFactory, Order currentOrder)
    {
        this.scanner = scanner;
        this.catalog = catalog;
        this.productFactory = productFactory;
        this.currentOrder = currentOrder;
    }

    public Scanner getScanner() { return scanner; }
    public ProductFactory getProductFactory() { return productFactory; }
    public Catalog getCatalog() { return catalog; }
    public Order getCurrentOrder() { return currentOrder; }

    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }

    public void displaySection(String title)
    {
        String divider = "-----------------------------------------------------------";
        System.out.println("\n" + divider);
        System.out.println("  " + title);
        System.out.println(divider);
    }

    public void displayError(String message)
    {
        System.out.println("❌ " + message);
    }

    public void displaySuccess(String message)
    {
        System.out.println("✅ " + message);
    }
}