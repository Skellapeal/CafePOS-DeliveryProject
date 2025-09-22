package Test;

import Main.Common.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

public class MoneyTest
{
    @Test
    public void ExceptionMoneyNegativeTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(BigDecimal.valueOf(-10));
        });
    }
    @Test
    public void ExceptionMoneyNullTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(null);
        });
    }
    @Test
    public void ExceptionAdditionIsNullTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.add(null);
        });
    }
    @Test
    public void ExceptionAdditionIsNegativeTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.add(Money.of(-1));
        });
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.subtract(null);
        });
    }
    @Test
    public void ExceptionSubtractionIsNegativeTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(BigDecimal.valueOf(10));
            Money result = money.subtract(Money.of(-1));
        });
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