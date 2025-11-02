package main.java.com.cafepos.printing;

import main.java.com.cafepos.legacy.LegacyThermalPrinter;

import java.nio.charset.StandardCharsets;

public class LegacyPrinterAdapter implements Printer
{
    private final LegacyThermalPrinter adaptee;

    public LegacyPrinterAdapter(LegacyThermalPrinter adaptee)
    {
        this.adaptee = adaptee;
    }

    @Override
    public void print(String receiptText)
    {
        byte[] escpos = receiptText.getBytes(StandardCharsets.UTF_8);
        adaptee.legacyPrint(escpos);
    }
}