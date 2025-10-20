package main.java.com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money implements Comparable<Money>
{
    private final BigDecimal amount;
    public static Money of(double value)
    {
        return new Money(BigDecimal.valueOf(value));
    }
    public static Money zero()
    {
        return new Money(BigDecimal.ZERO);
    }

    public Money(BigDecimal a)
    {
        if (a == null)
        {
            throw new IllegalArgumentException("Amount required");
        }
        else if(a.doubleValue() < 0)
        {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Class: Money for addition is null.");
        }
        return new Money(other.amount.add(amount));
    }

    public Money subtract(Money other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Class: Money for subtraction is null.");
        }
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(int qty)
    {
        if (qty < 0)
        {
            throw new IllegalArgumentException("Class: Money for *Multiplication* is negative.");
        }
        BigDecimal quant = new BigDecimal(qty);
        return new Money(quant.multiply(amount));
    }

    public Money divide(int qty)
    {
        if (qty < 0)
        {
            throw new IllegalArgumentException("Class: Money for *Division* is negative.");
        }
        BigDecimal quant = new BigDecimal(qty);
        return new Money(amount.divide(quant, 2, RoundingMode.HALF_UP));
    }

    @Override
    public int compareTo(Money o)
    {
        if(amount.compareTo(o.amount) < 0)
        {
            return -1;
        }
        else if (amount.compareTo(o.amount) > 0)
        {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(!(o instanceof Money otherMoney)) return false;
        return amount.equals(otherMoney.amount);
    }

    @Override
    public String toString()
    {
        return amount.toPlainString();
    }
}