package main.java.com.cafepos.app;

import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.domain.pricing.PricingService;

public final class CheckoutService
{
    private final OrderRepository orders;
    private final PricingService pricing;

    public CheckoutService(OrderRepository orders, PricingService pricing)
    {
        this.orders = orders;
        this.pricing = pricing;
    }

    public String checkout(long orderId, int taxPercent)
    {
        Order order = orders.findById(orderId).orElseThrow();
        var pr = pricing.price(order.subtotal());
        return new ReceiptFormatter().format(orderId, order.items(), pr, taxPercent);
    }
}

/*
import main.java.com.cafepos.domain.catalog.Product;
import main.java.com.cafepos.checkout.PricingService;
import main.java.com.cafepos.infra.ReceiptPrinter;
import main.java.com.cafepos.domain.value.Money;
import main.java.com.cafepos.domain.decorator.Priced;
import main.java.com.cafepos.app.factory.ProductFactory;

public final class CheckoutService
{

    private final ProductFactory factory;
    private final PricingService pricing;
    private final ReceiptPrinter printer;
    private final int taxPercent;

    public CheckoutService(ProductFactory factory, PricingService pricing, ReceiptPrinter printer, int taxPercent)
    {
        this.factory = factory;
        this.pricing = pricing;
        this.printer = printer;
        this.taxPercent = taxPercent;
    }

    public String checkout(String recipe, int qty)
    {
        Product product = factory.create(recipe);
        if (qty <= 0) qty = 1;

        Money unit = (product instanceof Priced p) ? p.price() : product.basePrice();
        Money subtotal = unit.multiply(qty);
        var result = pricing.price(subtotal);
        return printer.format(recipe, qty, result, taxPercent);
    }
}*/