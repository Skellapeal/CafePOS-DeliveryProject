package Main.Domain;

import Main.Common.Money;
import Main.Payment.PaymentStrategy;

import java.util.ArrayList;
import java.util.List;

public class Order
{
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    public Order(long id) { this.id = id; }

    public void addItem(LineItem li)
    {
        items.add(li);
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