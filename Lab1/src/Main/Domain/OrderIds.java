package Main.Domain;

public class OrderIds
{
    private static long sequence = 1001L;
    public static long next()
    {
        return sequence++;
    }
}