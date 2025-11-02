package main.java.com.cafepos.checkout;

import main.java.com.cafepos.common.Money;

public class NoDiscountPolicy implements DiscountPolicy
{
    @Override
    public Money calculateDiscount(Money subtotal)
    {
        return Money.zero();
    }
}
