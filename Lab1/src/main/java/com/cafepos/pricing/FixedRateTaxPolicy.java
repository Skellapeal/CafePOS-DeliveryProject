package main.java.com.cafepos.pricing;

import main.java.com.cafepos.domain.Money;

public class FixedRateTaxPolicy implements TaxPolicy
{
    private final int percent;

    public FixedRateTaxPolicy(int percent)
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