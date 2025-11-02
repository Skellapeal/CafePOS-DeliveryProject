package main.java.com.cafepos.order;

import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.payment.PaymentStrategy;

import java.util.ArrayList;
import java.util.List;

public class Order implements OrderPublisher
{
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    public Order(long id) { this.id = id; }
    private final List<OrderObserver> observers = new ArrayList<>();
/*
code for Observer management
 */
    @Override
    public void registerOrder(OrderObserver o)
    {
        if  (o == null)
        {
            throw new IllegalArgumentException("orderObserver cannot be null for registering");
        }
        if  (observers.contains(o))
        {
            return;
        }
        observers.add(o);
    }

    @Override
    public void unregisterOrder(OrderObserver o)
    {
        if  (o == null)
        {
            throw new IllegalArgumentException("orderObserver cannot be null for unregistering");
        }
        if  (!observers.contains(o))
        {
            return;
        }
        observers.remove(o);
    }
    @Override
    public void notifyObservers(String event)
    {
        for(OrderObserver o : observers)
        {
            o.updated(this, event);
        }
    }

    public void markReady()
    {
        notifyObservers("ready");
    }

/*
code for order management
 */
    public void addItem(LineItem li)
    {
        items.add(li);
        notifyObservers("itemAdded");
    }

    public Money subtotal()
    {
        return items.stream().map(LineItem::lineTotal).reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent)
    {
        if(percent < 0)
        {
            throw new IllegalArgumentException("Percent must be greater than 0");
        }
        return subtotal().multiply(percent).divide(100);
    }

    public Money totalWithTax(int percent)
    {
        if(percent < 0)
        {
            throw new IllegalArgumentException("Percent must be greater than 0");
        }
        return taxAtPercent(percent).add(subtotal());
    }

    public void pay(PaymentStrategy paymentMethod)
    {
        if(paymentMethod == null)
        {
            throw new IllegalArgumentException("Payment method required");
        }
        paymentMethod.pay(this);
        notifyObservers("paid");
    }

    public String id()
    {
        return Long.toString(id);
    }

    public List<LineItem> items()
    {
        return items;
    }
}