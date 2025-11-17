package main.java.com.cafepos.catalog;

import main.java.com.cafepos.domain.Money;

public interface Product
{
    String id();
    String name();
    Money basePrice();
}
