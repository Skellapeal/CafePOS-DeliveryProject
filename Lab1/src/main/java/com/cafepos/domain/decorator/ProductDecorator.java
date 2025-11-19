package main.java.com.cafepos.domain.decorator;

import main.java.com.cafepos.domain.catalog.Product;
import main.java.com.cafepos.domain.value.Money;

public abstract class ProductDecorator implements Product {
    protected final Product base;

    protected ProductDecorator(Product base)
    {
        if (base == null) throw new IllegalArgumentException("base product required");
        this.base = base;
    }

    @Override
    public String id()
    {
        return base.id();
    }

    @Override
    public Money basePrice()
    {
        return base.basePrice();
    }
}
