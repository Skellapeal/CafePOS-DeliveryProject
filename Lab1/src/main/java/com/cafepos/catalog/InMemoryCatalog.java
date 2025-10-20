package main.java.com.cafepos.catalog;

import java.util.*;
public final class InMemoryCatalog implements Catalog
{
    private final Map<String, Product> byId = new HashMap<>();

    @Override
    public void add(Product p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("Product required");
        }
        byId.put(p.id(), p);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public String listProducts()
    {
        StringBuilder productList = new StringBuilder();
        int counter = 0;
        for (Product p : byId.values())
        {
            counter++;
            productList.append(counter).append(".").append(p.name()).append("\n");
        }

        return productList.toString();
    }
}