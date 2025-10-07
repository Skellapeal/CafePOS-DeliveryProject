package Main.Decorator;

import Main.Catalog.Product;
import Main.Common.Money;
import Main.Domain.Priced;

public class ExtraShot extends ProductDecorator implements Priced
{
    private static final Money SURCHARGE = Money.of(0.80);
    public ExtraShot(Product base)
    {
        super(base);
        base.basePrice().add(SURCHARGE);
    }

    @Override
    public String name()
    {
        return base.name() + " + Extra Shot";
    }

    public Money price()
    {
        return (base instanceof Priced p? p.price() : base.basePrice()).add(SURCHARGE);}
    }

