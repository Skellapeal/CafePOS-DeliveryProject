package main.java.com.cafepos.pricing;

import main.java.com.cafepos.common.Money;

public class LoyalDiscountPolicy implements DiscountPolicy
{
    private final int percent;
    public LoyalDiscountPolicy(int percent)
    {
        this.percent = percent;
    }

    @Override
    public Money calculateDiscount(Money subtotal)
    {
        return subtotal.multiply(percent).divide(100);
    }
}
