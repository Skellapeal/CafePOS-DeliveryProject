package main.java.com.cafepos.commands;

import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.payment.*;
import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.payment.CardPayment;
import main.java.com.cafepos.payment.CashPayment;
import main.java.com.cafepos.payment.PaymentStrategy;
import main.java.com.cafepos.payment.WalletPayment;

public class PayCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        if (context.getCurrentOrder().items().isEmpty())
        {
            context.displayError("Cannot pay for empty order");
            return;
        }

        new ViewOrderCommand().execute(new String[0], context);

        System.out.println("\nPayment Options:");
        System.out.println("   1. CASH   - Cash payment");
        System.out.println("   2. CARD   - Credit/Debit card");
        System.out.println("   3. WALLET - Digital wallet");

        System.out.print("\nSelect payment method (1-3): ");
        String choice = context.getScanner().nextLine().trim();

        PaymentStrategy strategy = switch (choice)
        {
            case "1", "CASH" -> new CashPayment();
            case "2", "CARD" -> promptForCard(context);
            case "3", "WALLET" -> promptForWallet(context);
            default ->
            {
                context.displayError("Invalid payment method");
                yield null;
            }
        };

        if (strategy != null)
        {
            context.getCurrentOrder().pay(strategy);
            System.out.println("\nPayment successful! Thank you for your order!");

            Order newOrder = new Order(OrderIds.next());
            newOrder.registerOrder(new KitchenDisplay());
            newOrder.registerOrder(new DeliveryDesk());
            newOrder.registerOrder(new CustomerNotifier());
            context.setCurrentOrder(newOrder);
        }
    }

    private PaymentStrategy promptForCard(CommandContext context)
    {
        System.out.print("Enter card number: ");
        String cardNumber = context.getScanner().nextLine().trim();
        return cardNumber.isEmpty() ? null : new CardPayment(cardNumber);
    }

    private PaymentStrategy promptForWallet(CommandContext context)
    {
        System.out.print("Enter wallet ID: ");
        String walletId = context.getScanner().nextLine().trim();
        return walletId.isEmpty() ? null : new WalletPayment(walletId);
    }
}