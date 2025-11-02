package main.java.com.cafepos.demo;

import main.java.com.cafepos.catalog.Catalog;
import main.java.com.cafepos.catalog.InMemoryCatalog;
import main.java.com.cafepos.catalog.SimpleProduct;
import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.order.LineItem;
import main.java.com.cafepos.order.Order;
import main.java.com.cafepos.order.OrderIds;

public final class Week2Demo
{
    public static void main(String[] args)
    {
        Catalog catalog = new InMemoryCatalog();

        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50)));

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 1));

        int taxPct = 10;
        System.out.println("Order #" + order.id());
        System.out.println("Items: " + order.items().size());
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (" + taxPct + "%): " + order.taxAtPercent(taxPct));
        System.out.println("Total: " + order.totalWithTax(taxPct));
    }
}