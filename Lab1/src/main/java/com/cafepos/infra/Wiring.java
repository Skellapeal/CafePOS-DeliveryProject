package main.java.com.cafepos.infra;

import main.java.com.cafepos.app.CheckoutService;
import main.java.com.cafepos.domain.*;
import main.java.com.cafepos.pricing.*;

public final class Wiring
{
    public static record Components (OrderRepository repo, PricingService pricing, CheckoutService checkout)
    { }

    public static Components createDefault()
    {
        OrderRepository repo = new InMemoryOrderRepository();
        PricingService pricing = new PricingService(new LoyaltyPercentDiscount(5), new FixedRateTaxPolicy(10));
        CheckoutService checkout = new CheckoutService(repo, pricing);
        return new Components(repo, pricing, checkout);
    }
}
