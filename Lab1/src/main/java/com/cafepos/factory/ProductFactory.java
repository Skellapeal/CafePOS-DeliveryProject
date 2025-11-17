package main.java.com.cafepos.factory;

import main.java.com.cafepos.catalog.Product;
import main.java.com.cafepos.catalog.SimpleProduct;
import main.java.com.cafepos.domain.Money;
import main.java.com.cafepos.decorator.ExtraShot;
import main.java.com.cafepos.decorator.OatMilk;
import main.java.com.cafepos.decorator.SizeLarge;
import main.java.com.cafepos.decorator.Syrup;

public final class ProductFactory
{
    public Product create(String recipe)
    {
        if (recipe == null || recipe.isBlank()) throw new IllegalArgumentException("recipe required");

        String[] raw = recipe.split("\\s");
        String[] parts = java.util.Arrays.stream(raw).map(String::trim).map(String::toUpperCase).toArray(String[]::new);

        Product product = switch (parts[0])
        {
            case "ESP" -> new SimpleProduct("P-ESP", "Espresso",Money.of(2.50));
            case "LAT" -> new SimpleProduct("P-LAT", "Latte",Money.of(3.20));
            case "CAP" -> new SimpleProduct("P-CAP","Cappuccino", Money.of(3.00));

            default -> throw new
                    IllegalArgumentException("Unknown base: " + parts[0]);
        };
        for (int i = 1; i < parts.length; i++)
        {
            product = switch (parts[i])
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
