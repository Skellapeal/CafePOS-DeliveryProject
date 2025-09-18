package Test;

import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SimpleProductTest
{
    @Test
    public void ExceptionSimpleProductTest()
    {
        try
        {
            SimpleProduct simpleProduct = new SimpleProduct("SQE","Test", Money.of(-1));
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception for SimpleProduct: " + e.getMessage());
        }
    }
    @Test
    public void SimpleProductTest()
    {
        SimpleProduct simpleProduct = new SimpleProduct("SQE","Test", Money.of(1));

        Assertions.assertEquals("SQE",simpleProduct.id());
        Assertions.assertEquals("Test",simpleProduct.name());
        Assertions.assertEquals("1.00",simpleProduct.basePrice().toString());
    }
}
