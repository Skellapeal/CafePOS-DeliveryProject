package main.java.com.cafepos.pricing;

import main.java.com.cafepos.domain.Money;

public interface TaxPolicy
{
    Money taxOn(Money amount);
}
