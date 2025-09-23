package Main.Demo;

import Main.Catalog.Catalog;
import Main.Catalog.InMemoryCatalog;
import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import Main.Domain.LineItem;
import Main.Domain.Order;
import Main.Domain.OrderIds;
import Main.Payment.CardPayment;
import Main.Payment.CashPayment;
import Main.Payment.WalletPayment;

public class Week3Demo
{
    public static void main(String[] args)
    {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50)));

        // Cash payment
        Order order1 = new Order(OrderIds.next());
        order1.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order1.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 1));
        System.out.printf("\nOrder #%s \nTotal: %s\n", order1.id(), order1.totalWithTax(10));
        order1.pay(new CashPayment());

        // Card payment
        Order order2 = new Order(OrderIds.next());
        order2.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order2.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 1));
        System.out.printf("\nOrder #%s \nTotal: %s\n", order2.id(), order2.totalWithTax(10));
        order2.pay(new CardPayment("1234567812341234"));

        //Wallet payment
        Order order3 = new Order(OrderIds.next());
        order3.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order3.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 1));
        System.out.printf("\nOrder #%s \nTotal: %s\n", order3.id(), order3.totalWithTax(10));
        order3.pay(new WalletPayment("Neil&Jan-Joint-Financial-Account"));
    }
}
