package main.java.com.cafepos.pricing;

import main.java.com.cafepos.domain.Money;

public class LoyaltyPercentDiscount implements DiscountPolicy
{
    private final int percent;
    public LoyaltyPercentDiscount(int percent)
    {
        this.percent = percent;
    }

    @Override
    public Money calculateDiscount(Money subtotal)
    {
        return subtotal.multiply(percent).divide(100);
    }
}
