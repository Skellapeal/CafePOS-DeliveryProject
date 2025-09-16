package Main.Domain;

import Main.Common.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order
{
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    public Order(long id) { this.id = id; }

    public void addItem(LineItem li)
    {
        if (li.quantity() <= 0)
        {
            throw new IllegalArgumentException("cannot add item quantity of 0 or less");
        }
        items.add(li);
    }

    public Money subtotal()
    {
        return items.stream().map(LineItem::lineTotal).reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent)
    {
        Money totalCost = new Money(BigDecimal.valueOf(0));

        for(LineItem li: items)
        {
            totalCost.add(li.lineTotal());
        }

        totalCost = ;
    }
    public Money totalWithTax(int percent) { ... }
}
