package main.java.com.cafepos.demo;

import main.java.com.cafepos.app.commands.*;
import main.java.com.cafepos.app.events.*;
import main.java.com.cafepos.app.state.OrderFSM;
import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.domain.menu.*;
import main.java.com.cafepos.domain.payment.*;
import main.java.com.cafepos.domain.value.Money;
import main.java.com.cafepos.infrastructure.*;
import main.java.com.cafepos.ui.OrderController;

import java.util.*;

public final class CafePOSTerminalFINAL
{
    // Output Formatting
    private static final int TAX_PERCENT = 10;
    private static final String SEPARATOR = "────────────────────────────────────────────";
    private static final String THICK_SEPARATOR = "════════════════════════════════════════════";

    // Order Management
    private Order currentOrder;
    private OrderFSM orderStatus;
    private OrderService service;

    // Observers
    private KitchenDisplay kitchen;
    private CustomerNotifier customer;
    private DeliveryDesk delivery;

    // Command Management
    private PosRemote commandOperator;
    private int commandCount = 0;

    // Menu
    private Menu rootMenu;
    private Map<String, MenuItem> menuItemMap;

    // MVC
    private OrderController controller;
    private EventsBus bus;

    public static void main(String[] args)
    {
        new CafePOSTerminalFINAL().run();
    }

    private void run()
    {
        Scanner scanner = new Scanner(System.in);
        initialiseMenuSystem();
        printWelcome();
        createNewOrder();
        printHelp();

        while(true)
        {
            System.out.print("\n[Order #" + currentOrder.id() + "] > ");
            String input = scanner.nextLine().trim();

            if(input.isEmpty()) continue;

            commandCount++;
            String[] tokens = input.split("\\s+", 2);
            String command = tokens[0].toLowerCase();

            try
            {
                log("UI", "User Input: '" + input + "'");
                switch(command)
                {
                    case "add" -> handleAdd(tokens.length > 1 ? tokens[1] : "");
                    case "menu" -> displayFullMenu();
                    case "veg" -> displayVegetarianOnly();
                    case "mods" -> listModifiers();
                    case "pay" ->
                    {
                        String method = tokens.length > 1 ? tokens[1].toLowerCase() : "cash";
                        handlePay(method);
                        createNewOrder();
                    }
                    case "undo" -> commandOperator.undo();
                    case "status" -> displayOrderStatus();
                    case "items" -> displayItems();
                    case "help" -> printHelp();
                    case "q", "x", "exit", "quit" ->
                    {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Unknown Command: '" + command + "'. Type 'help' for available commands.");
                }
            }
            catch(Exception e)
            {
                throw new IllegalArgumentException();
            }
        }
    }

    private void initialiseMenuSystem()
    {
        log("APP", "Initialising Composite Menu System");

        rootMenu = new Menu("CAFÉ MENU");
        menuItemMap = new HashMap<>();

        // Coffee Menu
        Menu coffeeMenu = new Menu(" Coffee ");
        MenuItem espresso = new MenuItem("Espresso", Money.of(2.50), true);
        MenuItem latte = new MenuItem("Latte", Money.of(3.90), true);
        MenuItem cappuccino = new MenuItem("Cappuccino", Money.of(4.00), true);
        MenuItem americano = new MenuItem("Americano", Money.of(5.00), true);

        coffeeMenu.add(espresso);
        coffeeMenu.add(latte);
        coffeeMenu.add(cappuccino);
        coffeeMenu.add(americano);

        menuItemMap.put("espresso", espresso);
        menuItemMap.put("latte", latte);
        menuItemMap.put("cappuccino", cappuccino);
        menuItemMap.put("americano", americano);

        // Drinks Menu
        Menu drinksMenu = new Menu(" Drinks ");
        MenuItem cola = new MenuItem("Cola", Money.of(2.10), true);
        MenuItem clubOrange = new MenuItem("Club Orange", Money.of(1.80), true);
        MenuItem milk = new MenuItem("Milk", Money.of(1.50), true);

        drinksMenu.add(cola);
        drinksMenu.add(clubOrange);
        drinksMenu.add(milk);
        drinksMenu.add(coffeeMenu);

        menuItemMap.put("cola", cola);
        menuItemMap.put("orange", clubOrange);
        menuItemMap.put("milk", milk);

        // Desserts Menu
        Menu desserts = new Menu(" Desserts ");
        MenuItem chocolate = new MenuItem("Chocolate", Money.of(2.50), true);
        MenuItem iceCream = new MenuItem("Ice Cream", Money.of(1.50), true);
        MenuItem cake = new MenuItem("Cake", Money.of(4.50), true);

        desserts.add(chocolate);
        desserts.add(iceCream);
        desserts.add(cake);

        menuItemMap.put("chocolate", chocolate);
        menuItemMap.put("ice-cream", iceCream);
        menuItemMap.put("cake", cake);

        rootMenu.add(drinksMenu);
        rootMenu.add(desserts);

        log("APP", "Initialised Composite Menu System: Drinks > Coffee, Desserts");
        log("APP", "Options Total: " + menuItemMap.size());
    }

    private void displayFullMenu()
    {
        System.out.println("\n" + THICK_SEPARATOR);
        rootMenu.print();
        System.out.println(THICK_SEPARATOR + "\n");
    }

    private void displayVegetarianOnly()
    {
        System.out.println("\n" + SEPARATOR);
        System.out.println("VEGETARIAN OPTIONS");
        System.out.println(SEPARATOR);

        var vegItems = rootMenu.vegetarianItems();
        if(vegItems.isEmpty())
        {
            System.out.println("(no vegetarian items)");
        }
        else
        {
            for(MenuItem item : vegItems)
            {
                System.out.printf("(V) %s = %s%n", item.name(), item.price());
            }
        }
        System.out.println(SEPARATOR + "\n");
    }

    private void handleAdd(String args)
    {
        String[] parts = args.split("\\s+");
        if(parts.length < 1 || parts[0].isEmpty())
        {
            System.out.println("Usage: add <item_name> [modifier] [modifier] ... [qty]");
            System.out.println("Example: add espresso shot syr 2");
            return;
        }

        String itemName = parts[0].toLowerCase();
        MenuItem baseItem = menuItemMap.get(itemName);

        if(baseItem == null)
        {
            System.out.println("Unknown item: " + itemName + ". Try 'menu'");
            return;
        }

        List<String> modifiers = new ArrayList<>();
        int qty = 1;

        for(int i = 1; i < parts.length; i++)
        {
            String part = parts[i].toLowerCase();
            if(part.matches("^[a-z]+$"))
            {
                modifiers.add(part);
            }
            else
            {
                try
                {
                    qty = Integer.parseInt(part);
                    if(qty <= 0) throw new NumberFormatException();
                }
                catch(NumberFormatException e)
                {
                    System.out.println("Invalid quantity. Must be a positive integer");
                    return;
                }
            }
        }

        String recipe = itemName;
        if(!modifiers.isEmpty())
        {
            recipe = itemName;
            for(var modifier : modifiers)
            {
                recipe += " " + modifier;
            }
        }

        ICommand cmd = new AddItemCommand(service, recipe, qty);
        log("APP", "Queueing to PosRemote");

        commandOperator.setSlot(0, cmd);
        log("APP", "Pressing slot 0");

        commandOperator.press(0);
        log("APP", "Notifying Observers");
        bus.emit(new OrderItemAdded(currentOrder.id()));
    }

    private void handlePay(String method)
    {
        if(currentOrder.items().isEmpty())
        {
            System.out.println("Cannot pay: no items in order");
            return;
        }

        PaymentStrategy strategy = switch (method)
        {
            case "cash" ->
            {
                log("DOMAIN", "Creating CashPayment strategy");
                yield new CashPayment();
            }
            case "wallet" ->
            {
                log("DOMAIN", "Creating WalletPayment strategy");
                yield new WalletPayment("Neil&Jan Joint-Wallet");
            }
            case "card" ->
            {
                log("DOMAIN", "Creating CardPayment strategy");
                yield new CardPayment("1239084571020398");
            }
            default ->
            {
                System.out.println("Unknown payment method. Use: cash, card, or wallet");
                yield null;
            }
        };

        if (strategy == null) return;

        ICommand payCommand = new PayCommand(service, strategy, TAX_PERCENT);

        log("APP", "Queueing PayCommand to PosRemote");
        commandOperator.setSlot(1, payCommand);

        log("APP", "Pressing slot 1");
        commandOperator.press(1);

        log("APP", "Payment processed.");

        orderStatus.pay();

        System.out.println("\n" + SEPARATOR);
        bus.emit(new OrderPaid(currentOrder.id()));
        System.out.println(SEPARATOR + "\n");

        orderStatus.markReady();
        orderStatus.deliver();

        System.out.println(printReceipt());
    }

    private void displayOrderStatus()
    {
        System.out.println("\n" + THICK_SEPARATOR);
        System.out.println("ORDER STATUS");
        System.out.println(THICK_SEPARATOR);
        System.out.println("Order ID:     " + currentOrder.id());
        System.out.println("FSM State:    " + orderStatus.status());
        System.out.println("Item Count:   " + currentOrder.items().size());
        System.out.println(SEPARATOR);
        System.out.printf("Subtotal:     %s%n", currentOrder.subtotal());
        System.out.printf("Tax (10%%):    %s%n", currentOrder.taxAtPercent(TAX_PERCENT));
        System.out.printf("Total:        %s%n", currentOrder.totalWithTax(TAX_PERCENT));
        System.out.println(THICK_SEPARATOR + "\n");
    }

    private void displayItems()
    {
        System.out.println("\n" + SEPARATOR);
        System.out.println("ORDER ITEMS");
        System.out.println(SEPARATOR);

        if (currentOrder.items().isEmpty())
        {
            System.out.println("(no items)");
        }
        else
        {
            for (LineItem item : currentOrder.items())
            {
                System.out.printf("  • %s x%d = %s%n",
                        item.product().name(),
                        item.quantity(),
                        item.lineTotal());
            }
        }
        System.out.println(SEPARATOR + "\n");
    }

    private void listModifiers()
    {
        System.out.println("\n" + SEPARATOR);
        System.out.println("MODIFIERS");
        System.out.println(SEPARATOR);
        System.out.println("  shot     (€0.80)");
        System.out.println("  oat      (€0.50)");
        System.out.println("  syr      (€0.40)");
        System.out.println("  l        (€0.70)");
        System.out.println("\nCompose: add espresso shot syr 2");
        System.out.println(SEPARATOR + "\n");
    }

    private void createNewOrder()
    {
        currentOrder = new Order(OrderIds.next());
        orderStatus = new OrderFSM();
        service = new OrderService(currentOrder);
        commandOperator = new PosRemote(10);

        log("APP", "Initialising events, repository & checkout service");
        Wiring.Components wiring = Wiring.createDefault();
        controller =  new OrderController(wiring.repo(), wiring.checkout());
        controller.createOrder(currentOrder.id());

        log("APP", "OrderFSM created (state: " + orderStatus.status() + ")");
        log("APP", "PosRemote created");
        initialiseEventObservers();
    }

    private void initialiseEventObservers()
    {
        kitchen = new KitchenDisplay();
        customer = new CustomerNotifier();
        delivery = new DeliveryDesk();

        bus = new EventsBus();

        bus.on(OrderItemAdded.class, _ ->
        {
            kitchen.updated(currentOrder, "itemAdded");
            customer.updated(currentOrder, "itemAdded");
        });

        bus.on(OrderReady.class, _ ->
        {
            customer.updated(currentOrder, "ready");
            delivery.updated(currentOrder, "ready");
        });

        bus.on(OrderPaid.class, _ ->
        {
            kitchen.updated(currentOrder, "paid");
            customer.updated(currentOrder, "paid");
        });

        log("INFRA", "Observers wired to EventsBus");
    }

    private void log(String layer, String message)
    {
        String timestamp = String.format("[%02d] ", commandCount);
        String mvcLayer = switch(layer)
        {
            case "UI" -> "[UI]     ";
            case "APP"  -> "[APP]     ";
            case "DOMAIN" -> "[DOMAIN]     ";
            case "INFRA" -> "[INFRA]     ";
            default -> "[" + layer + "]     ";
        };
        System.out.println(timestamp + mvcLayer + message);
    }

    private void printWelcome()
    {
        System.out.println("\n" + THICK_SEPARATOR);
        System.out.println("              Welcome to Starbuzz Café");
        System.out.println(THICK_SEPARATOR);
    }

    private void printHelp()
    {
        System.out.println("\n" + THICK_SEPARATOR);
        System.out.println("COMMANDS");
        System.out.println(THICK_SEPARATOR);

        System.out.println("\n[MENU]");
        System.out.println("  menu                         Full hierarchical menu");
        System.out.println("  veg                          Vegetarian items only");
        System.out.println("  list                         Quick item names");

        System.out.println("\n[ORDER MANAGEMENT]");
        System.out.println("  add <item> [+mod] [qty]      Add item with modifiers");
        System.out.println("  mods                         Show modifiers");
        System.out.println("  items                        Display current items");
        System.out.println("  status                       Order summary");

        System.out.println("\n[STATE MACHINE]");
        System.out.println("  pay [method]                 Process payment");

        System.out.println("\n[COMMANDS]");
        System.out.println("  undo                         Undo last command");

        System.out.println("\n[UTILITY]");
        System.out.println("  help                         Show this");
        System.out.println("  exit                         Quit");

        System.out.println("\n" + THICK_SEPARATOR + "\n");
    }

    private String printReceipt()
    {
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n").append(THICK_SEPARATOR);
        receipt.append("\nRECEIPT\n");
        receipt.append(THICK_SEPARATOR).append("\n");

        for(LineItem item : currentOrder.items())
        {
            receipt.append(String.format("%s x%d               %s%n", item.product().name(), item.quantity(), item.lineTotal()));
        }

        receipt.append(SEPARATOR).append("\n");
        receipt.append(String.format("  Subtotal: %s%n", currentOrder.subtotal()));
        receipt.append(String.format("  Tax (10%%): %s%n", currentOrder.taxAtPercent(TAX_PERCENT)));
        receipt.append(String.format("  Total: %s%n", currentOrder.totalWithTax(TAX_PERCENT)));
        receipt.append(THICK_SEPARATOR).append("\n");
        receipt.append("  Status: ").append(orderStatus.status()).append("\n");
        receipt.append(THICK_SEPARATOR);

        return receipt.toString();
    }
}