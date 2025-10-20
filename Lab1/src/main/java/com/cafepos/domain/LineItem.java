package main.java.com.cafepos.domain;

import main.java.com.cafepos.catalog.Product;
import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.decorator.Priced;

public final class LineItem
{
    private final Product product;
    private final int quantity;

    public LineItem(Product product, int quantity)
    {
        if (product == null)
        {
            throw new IllegalArgumentException("Product required");
        }
        if (quantity <= 0)
        {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product product() { return product; }
    public int quantity() { return quantity; }
    public Money lineTotal()
    {
        Money unit = (product instanceof Priced p) ? p.price():product.basePrice();
        return unit.multiply(quantity);
    }
}
