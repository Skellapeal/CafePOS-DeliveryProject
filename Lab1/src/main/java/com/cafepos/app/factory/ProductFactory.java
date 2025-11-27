package main.java.com.cafepos.app.factory;

import main.java.com.cafepos.domain.catalog.Product;
import main.java.com.cafepos.domain.catalog.SimpleProduct;
import main.java.com.cafepos.domain.value.Money;
import main.java.com.cafepos.domain.decorator.ExtraShot;
import main.java.com.cafepos.domain.decorator.OatMilk;
import main.java.com.cafepos.domain.decorator.SizeLarge;
import main.java.com.cafepos.domain.decorator.Syrup;

public final class ProductFactory
{
    public Product create(String recipe)
    {
        if (recipe == null || recipe.isBlank()) throw new IllegalArgumentException("recipe required");

        String[] raw = recipe.split("\\s+");
        String[] parts = java.util.Arrays.stream(raw).map(String::trim).map(String::toUpperCase).toArray(String[]::new);

        Product product = switch (parts[0].toLowerCase())
        {
            case "espresso" -> new SimpleProduct("P-ESP", "Espresso",Money.of(2.50));
            case "latte" -> new SimpleProduct("P-LAT", "Latte",Money.of(3.90));
            case "cappuccino" -> new SimpleProduct("P-CAP","Cappuccino", Money.of(4.00));
            case "americano" -> new SimpleProduct("P-AMR", "Americano",Money.of(5.00));
            case "cola" -> new SimpleProduct("P-COL", "Cola",Money.of(2.10));
            case "orange" -> new SimpleProduct("P-ORG","Club Orange", Money.of(1.80));
            case "milk" -> new SimpleProduct("P-MLK", "Milk",Money.of(1.50));
            case "chocolate" -> new SimpleProduct("P-CHO", "Chocolate",Money.of(2.50));
            case "ice-cream" -> new SimpleProduct("P-ICE","Ice Cream", Money.of(1.50));
            case "cake" -> new SimpleProduct("P-CAK", "Cake",Money.of(4.50));

            default -> throw new
                    IllegalArgumentException("Unknown base: " + parts[0]);
        };
        for (int i = 1; i < parts.length; i++)
        {
            product = switch (parts[i].toUpperCase())
            {
                case "SHOT" -> new ExtraShot(product);
                case "OAT" -> new OatMilk(product);
                case "SYP" -> new Syrup(product);
                case "L" -> new SizeLarge(product);
                default -> throw new
                        IllegalArgumentException("Unknown addon: " + parts[i]);
            };
        }
        return product;
    }
}
