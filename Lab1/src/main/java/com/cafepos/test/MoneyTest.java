package main.java.com.cafepos.test;

import main.java.com.cafepos.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertEquals(0, result.compareTo(Money.of(20.75)));
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

        assertEquals(0, result.compareTo(Money.of(7)));
    }
    @Test
    public void MultiplyMoneyTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.00));
        Money result = money.multiply(3); //expected 30

        assertEquals(0, result.compareTo(Money.of(30)));
    }
    @Test
    public void DivideMoneyTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.00));
        Money result = money.divide(5); // expected 2.00

        assertEquals(0, result.compareTo(Money.of(2)));
    }
    @Test
    public void ToStringMoneyTest()
    {
        Money money = new Money(BigDecimal.valueOf(10.00));
        String result = money.toString();

        assertEquals("10.00", result);
    }
    @Test
    public void ZeroMoneyTest()
    {
        Money money = new Money(BigDecimal.ZERO); // expected 0

        assertEquals(0, money.compareTo(Money.of(0)));
    }

    @Test
    public void testMoneyOfFactoryMethod()
    {
        Money money = new Money(BigDecimal.valueOf(15.99));
        assertEquals("15.99",  money.toString());
    }

    @Test
    public void testMoneyZeroFactoryMethod()
    {
        Money zeroMoney = Money.zero();
        assertEquals("0.00",  zeroMoney.toString());
        assertEquals(0, zeroMoney.compareTo(Money.of(0)));
    }

    @Test
    public void testPrecisionRounding()
    {
        Money money = new Money(BigDecimal.valueOf(10.999));
        assertEquals("11.00",  money.toString());
    }

    @Test
    public void testHalfUpRounding()
    {
        Money money = new Money(BigDecimal.valueOf(10.555));
        assertEquals("10.56",  money.toString());
    }

    @Test
    public void testMultiplyByZero()
    {
        Money money = Money.of(50.00);
        Money result = money.multiply(0);
        assertEquals(0, result.compareTo(Money.zero()));
    }

    @Test
    public void testDivideByOne()
    {
        Money money = Money.of(42.50);
        Money result = money.divide(1);
        assertEquals(0, result.compareTo(money));
    }

    @Test
    public void testChainedOperations()
    {
        Money initialAmount = Money.of(100.00);
        Money result = initialAmount.add(Money.of(100.00)
                .subtract(Money.of(50.00))
                .multiply(2));
        assertEquals(0, result.compareTo(Money.of(200.00)));
    }

    @Test
    public void testCompareToGreaterThan()
    {
        Money larger = Money.of(50.00);
        Money smaller = Money.of(25.00);
        assertEquals(1, larger.compareTo(smaller));
    }

    @Test
    public void testCompareToLessThan()
    {
        Money smaller =  Money.of(50.00);
        Money larger = Money.of(100.00);
        assertEquals(-1, smaller.compareTo(larger));
    }

    @Test
    public void testCompareToEqual()
    {
        Money money1 = Money.of(50.00);
        Money money2 = Money.of(50.00);
        assertEquals(0, money1.compareTo(money2));
    }

    @Test
    public void ExceptionMultiplyByNegativeTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = Money.of(10.00);
            money.multiply(-5);
        });
    }

    @Test
    public void ExceptionDivideByNegativeTest()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            Money money = Money.of(10.00);
            money.divide(-2);
        });
    }

    @Test
    public void testDivisionWithRemainder()
    {
        Money money = Money.of(10.00);
        Money result = money.divide(3);
        assertEquals("3.33", result.toString());
    }

    @Test
    public void testLargeNumberHandling()
    {
        Money largeMoney = Money.of(9999999999.99);
        Money result = largeMoney.add(Money.of(0.01));
        assertEquals("10000000000.00", result.toString());
    }
}