package main.java.com.cafepos.ui;

import main.java.com.cafepos.app.CheckoutService;
import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.app.factory.ProductFactory;

public final class OrderController
{
    private final OrderRepository repo;
    private final CheckoutService checkout;
    private final ProductFactory factory = new ProductFactory();

    public OrderController(OrderRepository repo, CheckoutService checkout)
    {
        this.repo = repo; this.checkout = checkout;
    }

    public long createOrder(long id)
    {
        repo.save(new Order(id));
        return id;
    }

    public void addItem(long orderId, String recipe, int qty)
    {
        Order order = repo.findById(orderId).orElseThrow();
        order.addItem(new LineItem(factory.create(recipe), qty));
        repo.save(order);
    }

    public String checkout(long orderId, int taxPercent)
    {
        return checkout.checkout(orderId, taxPercent);
    }
}
