package main.java.com.cafepos.domain.decorator;

import main.java.com.cafepos.domain.catalog.Product;
import main.java.com.cafepos.domain.value.Money;

public final class SizeLarge extends ProductDecorator implements Priced
{
    private static final Money SURCHARGE = Money.of(0.70);
    public SizeLarge(Product base)
    {
        super(base);
        base.basePrice().add(SURCHARGE);
    }

    @Override
    public String name()
    {
        return base.name() + " (Large)";
    }

    public Money price()
    {
        return (base instanceof Priced p? p.price() : base.basePrice()).add(SURCHARGE);
    }
}
