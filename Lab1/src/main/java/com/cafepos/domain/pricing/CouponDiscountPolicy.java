package main.java.com.cafepos.domain.pricing;

import main.java.com.cafepos.domain.value.Money;

public class CouponDiscountPolicy implements DiscountPolicy
{
    private double discountRate;
    public CouponDiscountPolicy(double discountRate)
    {
        this.discountRate = discountRate;
    }

    @Override
    public Money calculateDiscount(Money subtotal)
    {
        return Money.of(discountRate);
    }
}