package Main.Catalog;

import Main.Common.Money;

public interface Product
{
    String id();
    String name();
    Money basePrice();
}
