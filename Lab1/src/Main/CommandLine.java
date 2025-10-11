package Main;

import Main.Catalog.*;
import Main.Commands.*;
import Main.Common.Money;
import Main.Domain.*;
import Main.Factory.ProductFactory;
import Main.Payment.*;
import java.util.*;

public class CommandLine
{
    private final Scanner scanner;
    private final Catalog catalog;
    private final Map<String, ICommand> commands;
    private final CommandContext context;
    private Order currentOrder;
    private boolean isRunning;

    private static final String WELCOME_MESSAGE = """
            ===========================================================
                               WELCOME TO STARBUZZ CAFÉ
            ===========================================================
            """;

    private static final String PROMPT = "cafe> ";

    public CommandLine()
    {
        scanner = new Scanner(System.in);
        catalog = new InMemoryCatalog();
        ProductFactory productFactory = new ProductFactory();
        commands = new HashMap<>();
        isRunning = true;

        initialiseCatalog();
        createNewOrder();

        context = new  CommandContext(scanner, catalog, productFactory, currentOrder);

        registerCommands();
    }

    public static void main(String[] args)
    {
        new CommandLine().run();
    }

    public void run()
    {
        displayWelcome();

        while(isRunning)
        {
            displayPrompt();
            String input = scanner.nextLine().trim();

            if(input.isEmpty())
            {
                continue;
            }

            processInput(input);
        }
        cleanup();
    }

    private void processInput(String input)
    {
        String[] parts = input.split("\\s+");       // Catch any whitespace
        String commandName = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        if("exit".equals(commandName) || "quit".equals(commandName) || "q".equals(commandName) || "x".equals(commandName))
        {
            isRunning = false;
            new ExitCommand().execute(args, context);
            return;
        }

        ICommand command = commands.get(commandName);
        if(command != null)
        {
            try
            {
                context.setCurrentOrder(currentOrder);
                command.execute(args, context);
                currentOrder = context.getCurrentOrder();
            }
            catch(Exception e)
            {
                context.displayError("Command failed: " + e.getMessage());
            }
        }
        else
        {
            context.displayError("Unknown command: " + commandName);
            displayAvailableCommands();
        }
    }

    private void initialiseCatalog()
    {
        catalog.add(new SimpleProduct("ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("LAT", "Latte", Money.of(3.20)));
        catalog.add(new SimpleProduct("CAP", "Cappuccino", Money.of(3.00)));
    }

    private void registerCommands()
    {
        commands.put("help", new HelpCommand());
        commands.put("h", new HelpCommand());
        commands.put("menu", new MenuCommand());
        commands.put("m", new MenuCommand());
        commands.put("order", new OrderCommand());
        commands.put("o", new OrderCommand());
        commands.put("add", new AddItemCommand());
        commands.put("a", new AddItemCommand());
        commands.put("view", new ViewOrderCommand());
        commands.put("v", new ViewOrderCommand());
        commands.put("remove", new RemoveItemCommand());
        commands.put("r", new RemoveItemCommand());
        commands.put("pay", new PayCommand());
        commands.put("p", new PayCommand());
        commands.put("clear", new ClearOrderCommand());
        commands.put("exit", new ExitCommand());
        commands.put("quit", new ExitCommand());
        commands.put("q", new ExitCommand());
        commands.put("x", new ExitCommand());
    }

    private void createNewOrder()
    {
        currentOrder = new Order(OrderIds.next());
        currentOrder.registerOrder(new KitchenDisplay());
        currentOrder.registerOrder(new DeliveryDesk());
        currentOrder.registerOrder(new CustomerNotifier());
    }

    private static void displayWelcome()
    {
        System.out.println(WELCOME_MESSAGE);
        System.out.println("Type 'help' to see available commands or 'order' to start ordering!");
        System.out.println("Quick commands: menu (m), order (o), view (v), pay (p), quit (q)");
    }

    private static void displayAvailableCommands()
    {
        System.out.println("Available commands: help, menu, order, view, pay, quit");
    }

    private static void displayPrompt()
    {
        System.out.print("\n" + PROMPT);
    }

    private void cleanup()
    {
        scanner.close();
    }
}