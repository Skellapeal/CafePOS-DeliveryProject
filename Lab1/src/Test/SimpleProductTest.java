package Test;

import Main.Catalog.SimpleProduct;
import Main.Common.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleProductTest
{
    @Test
    public void ExceptionSimpleProductTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new SimpleProduct("SQE","Test", Money.of(-1)));
    }
    @Test
    public void SimpleProductValueTest()
    {
        SimpleProduct simpleProduct = new SimpleProduct("SQE","Test", Money.of(1));

        Assertions.assertEquals("SQE",simpleProduct.id());
        Assertions.assertEquals("Test",simpleProduct.name());
        Assertions.assertEquals("1.00",simpleProduct.basePrice().toString());
    }
}
