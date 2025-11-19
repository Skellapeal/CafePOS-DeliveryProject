package main.java.com.cafepos.domain.pricing;

import main.java.com.cafepos.domain.value.Money;

public class NoDiscountPolicy implements DiscountPolicy
{
    @Override
    public Money calculateDiscount(Money subtotal)
    {
        return Money.zero();
    }
}
