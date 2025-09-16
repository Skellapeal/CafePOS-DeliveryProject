package Main.Common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money implements Comparable<Money> {

    private final BigDecimal amount;

    public static Money of(double value)
    { ... }

    public static Money zero()
    { ... }

    public Money(BigDecimal a)
    {
        if (a == null) throw new IllegalArgumentException("amount required");
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other)
    {
        if(other == null || other.amount.doubleValue() < 0)
        {
            throw new IllegalArgumentException("check yo numbers");
        }
        return new Money(other.amount.add(amount));
    }

    public Money multiply(int qty)
    {
        BigDecimal quant = new BigDecimal(qty);
        return new Money(quant.multiply(amount));
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
