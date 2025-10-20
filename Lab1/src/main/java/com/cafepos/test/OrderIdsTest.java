package main.java.com.cafepos.test;

import main.java.com.cafepos.domain.OrderIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderIdsTest
{
    @Test
    public void testOrderIdGeneration()
    {
        long firstId = OrderIds.next();
        long secondId = OrderIds.next();

        assertTrue(firstId >= 1001L);
        assertEquals(firstId + 1, secondId);
    }

    @Test
    public void testOrderIdSequence()
    {
        long firstId = OrderIds.next();
        long secondId = OrderIds.next();
        long thirdId = OrderIds.next();

        assertEquals(firstId + 1, secondId);
        assertEquals(secondId + 1, thirdId);
        assertEquals(firstId + 2, thirdId);
    }

    @Test
    public void testOrderIdUniqueness()
    {
        long[] ids = new long[10];
        for(int i = 0; i < 10; i++)
        {
            ids[i] = OrderIds.next();
        }

        for(int i = 0; i < 10; i++)
        {
            for (int j = i + 1; j < ids.length; j++)
            {
                assertTrue(ids[i] != ids[j]);
            }
        }
    }

    @Test
    public void testOrderIdIncrementing()
    {
        long startId = OrderIds.next();
        for (int i = 1; i <= 5; i++)
        {
            long nextId = OrderIds.next();
            assertEquals(startId + i, nextId);
        }
    }
}