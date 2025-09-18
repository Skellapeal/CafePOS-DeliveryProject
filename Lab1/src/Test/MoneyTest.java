package Test;

import Main.Common.Money;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

public class MoneyTest
{
    @Test (expected = IllegalArgumentException.class)
    public void ExceptionMoneyNegativeTest()
    {
        Money money = new Money(BigDecimal.valueOf(-10));
    }
    @Test
    public void ExceptionMoneyNullTest()
    {
        try
        {
            Money money = new Money(null);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
    @Test
    public void ExceptionAdditionIsNullTest()
    {
        try
        {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.add(null);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
    @Test
    public void ExceptionAdditionIsNegativeTest()
    {
        try
        {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.add(Money.of(-1));
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
    @Test
    public void AdditionTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.50));
        Money result = money.add(Money.of(10.25)); // expected 20.75

        Assertions.assertEquals(0, result.compareTo(Money.of(20.75)));
    }
    @Test
    public void ExceptionSubtractionIsNullTest()
    {
        try
        {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.subtract(null);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
    @Test
    public void ExceptionSubtractionIsNegativeTest()
    {
        try
        {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.subtract(Money.of(-1));
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
    @Test
    public void SubtractMoneyTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.00));
        Money result = money.subtract(Money.of(3.00));

        Assertions.assertEquals(0, result.compareTo(Money.of(7)));
    }
    @Test
    public void MultiplyMoneyTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.00));
        Money result = money.multiply(3); //expected 30

        Assertions.assertEquals(0, result.compareTo(Money.of(30)));
    }
    @Test
    public void DivideMoneyTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.00));
        Money result = money.divide(5); // expected 2.00

        Assertions.assertEquals(0, result.compareTo(Money.of(2)));
    }
    @Test
    public void ToStringMoneyTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.00));
        String result = money.toString();

        Assertions.assertEquals("10.00", result);
    }
    @Test
    public void ZeroMoneyTest()
    {
        Money money = new Money(BigDecimal.ZERO); // expected 0

        Assertions.assertEquals(0, money.compareTo(Money.of(0)));
    }

}