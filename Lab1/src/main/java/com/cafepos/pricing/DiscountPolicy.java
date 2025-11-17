package main.java.com.cafepos.pricing;

import main.java.com.cafepos.domain.Money;

public interface DiscountPolicy
{
    Money calculateDiscount(Money subtotal);
}
