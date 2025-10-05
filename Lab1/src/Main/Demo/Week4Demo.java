package Main.Demo;

import Main.Catalog.Catalog;
import Main.Catalog.InMemoryCatalog;
import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import Main.Domain.*;
import Main.Payment.CashPayment;

public final class Week4Demo
{
    public static void main(String[] args)
    {
        Catalog catalog = new InMemoryCatalog();

        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        Order order = new Order(OrderIds.next());

        order.registerOrder(new KitchenDisplay());
        order.registerOrder(new DeliveryDesk());
        order.registerOrder(new CustomerNotifier());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 1));
        order.pay(new CashPayment());

        order.markReady();
    }
}

