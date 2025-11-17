package main.java.com.cafepos.decorator;

import main.java.com.cafepos.catalog.Product;
import main.java.com.cafepos.domain.Money;

public final class Syrup extends ProductDecorator implements Priced
{
    private static final Money SURCHARGE = Money.of(0.40);
    public Syrup(Product base)
    {
        super(base);
        base.basePrice().add(SURCHARGE);
    }

    @Override
    public String name()
    {
        return base.name() + " + Syrup";
    }

    public Money price()
    {
        return (base instanceof Priced p? p.price() : base.basePrice()).add(SURCHARGE);
    }
}
