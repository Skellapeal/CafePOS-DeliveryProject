package main.java.com.cafepos.commands;

public class HelpCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        context.displaySection("Available Commands");
        System.out.println("""
             General Commands:
                help, h          - Show this help message
                menu, m          - Display product menu
                exit, x, quit, q    - Exit the application

             Order Management:
                order, o         - Start interactive order process
                add, a           - Quick add item to order
                view, v          - View current order
                remove, r        - Remove item from order
                clear            - Clear current order

             Payment:
                pay, p           - Process payment for order

             Tips:
                - Most commands have short aliases (e.g., 'h' for help)
                - Use 'order' for guided product customization
                - Products can be customized: ESP SHOT OAT L (Large Espresso with extra shot and oat milk)
            """);
    }
}