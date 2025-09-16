package Main.Catalog;

import Main.Common.Money;

import java.math.BigDecimal;

public final class SimpleProduct implements Product
{
    private final String id;
    private final String name;
    private final Money basePrice;

    public SimpleProduct(String id, String name, Money basePrice)
    {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;

        if(basePrice.compareTo(new Money(BigDecimal.valueOf(0))) < 0)
        {
            throw new IllegalArgumentException("basePrice cant be less than 0");
        }

    }

    @Override public String id() { return id; }
    @Override public String name() { return name; }
    @Override public Money basePrice() { return basePrice; }
}

