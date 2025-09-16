package Main.Common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money implements Comparable<Money> {

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
        if (a == null) throw new IllegalArgumentException("amount required");
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Class: Money is null.");
        }
        else if (other.amount.doubleValue() < 0)
        {
            throw new IllegalArgumentException(other.amount + " is not a valid value for variable 'amount.'");
        }
        return new Money(other.amount.add(amount));
    }

    public Money subtract(Money other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Class: Money is null.");
        }
        else if (other.amount.doubleValue() < 0)
        {
            throw new IllegalArgumentException(other.amount + " is not a valid value for variable 'amount.'");
        }
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(int qty)
    {
        BigDecimal quant = new BigDecimal(qty);
        return new Money(quant.multiply(amount));
    }

    public Money divide(int qty)
    {
        BigDecimal quant = new BigDecimal(qty);
        return new Money(amount.divide(quant, 2, RoundingMode.HALF_UP));
    }

    @Override
    public int compareTo(Money o) {

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
// equals, hashCode, toString, etc.
}
