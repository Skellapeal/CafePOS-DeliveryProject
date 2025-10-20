package main.java.com.cafepos.demo;

import main.java.com.cafepos.catalog.Catalog;
import main.java.com.cafepos.catalog.InMemoryCatalog;
import main.java.com.cafepos.catalog.SimpleProduct;
import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.payment.CashPayment;

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

