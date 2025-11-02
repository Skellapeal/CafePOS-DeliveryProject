package main.java.com.cafepos.order;

public class OrderIds
{
    private static long sequence = 1001L;
    public static long next()
    {
        return sequence++;
    }
}