package main.java.com.cafepos.pricing;

import main.java.com.cafepos.common.Money;

public class ReceiptPrinter
{
    public String format(String recipe, int quantity, PricingService.PricingResult pricingResult, int taxPercent)
    {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(") x").append(quantity).append("\n");
        receipt.append("Subtotal: ").append(pricingResult.subtotal()).append("\n");
        if(pricingResult.discount().compareTo(Money.zero()) > 0)
        {
            receipt.append("Discount: -").append(pricingResult.discount()).append("\n");
        }

        receipt.append("Tax: (").append(taxPercent).append("%): ").append(pricingResult.tax()).append("\n");
        receipt.append("Total: ").append(pricingResult.total());
        return receipt.toString();
    }
}