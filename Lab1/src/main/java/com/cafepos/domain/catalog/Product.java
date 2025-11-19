package main.java.com.cafepos.domain.catalog;

import main.java.com.cafepos.domain.value.Money;

public interface Product
{
    String id();
    String name();
    Money basePrice();
}
