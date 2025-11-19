package main.java.com.cafepos.domain.pricing;

import main.java.com.cafepos.domain.value.Money;

public interface DiscountPolicy
{
    Money calculateDiscount(Money subtotal);
}
