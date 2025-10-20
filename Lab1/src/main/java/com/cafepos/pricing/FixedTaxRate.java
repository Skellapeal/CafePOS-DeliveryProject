package main.java.com.cafepos.pricing;

import main.java.com.cafepos.common.Money;

public class FixedTaxRate implements TaxPolicy
{
    private final int percent;

    public FixedTaxRate(int percent)
    {
        if(percent < 0 || percent > 100) throw new IllegalArgumentException();

        this.percent = percent;
    }

    @Override
    public Money taxOn(Money amount)
    {
        return amount.multiply(percent).divide(100);
    }
}