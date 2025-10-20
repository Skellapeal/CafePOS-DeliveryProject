package main.java.com.cafepos.pricing;

import main.java.com.cafepos.common.Money;

public class PricingService
{
    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;

    public PricingService(DiscountPolicy discountPolicy, TaxPolicy taxPolicy)
    {
        this.discountPolicy = discountPolicy;
        this.taxPolicy = taxPolicy;
    }

    public PricingResult price(Money subtotal)
    {
        Money discount = discountPolicy.calculateDiscount(subtotal);
        Money discounted = subtotal.subtract(discount).compareTo(Money.zero()) <= 0 ? Money.zero() : subtotal.subtract(discount);
        Money tax = taxPolicy.taxOn(discounted);
        Money total =  discounted.add(tax);

        return new PricingResult(subtotal, discount, tax, total);
    }

    public record PricingResult(Money subtotal, Money discount, Money tax, Money total)
    { }
}