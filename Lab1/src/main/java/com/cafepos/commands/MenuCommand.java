package main.java.com.cafepos.commands;

public class MenuCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        context.displaySection("Menu");
        System.out.println("Base Products:");
        System.out.println(context.getCatalog().listProducts());

        System.out.println("\nAvailable Customisations:");
        System.out.println("   SHOT  - Extra shot (+€0.50)");
        System.out.println("   OAT   - Oat milk (+€0.30)");
        System.out.println("   SYP   - Flavored syrup (+€0.25)");
        System.out.println("   L     - Large size (+€0.40)");

        System.out.println("\nExample: 'order LAT SHOT OAT' creates a Latte with extra shot and oat milk");
    }
}