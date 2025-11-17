package main.java.com.cafepos.infra;

public class LegacyThermalPrinter
{
    public void legacyPrint(byte[] payload)
    {
        System.out.println("[Legacy] printing bytes: " + payload.length);
    }
}