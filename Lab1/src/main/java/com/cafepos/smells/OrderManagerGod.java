package main.java.com.cafepos.smells;

import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.factory.ProductFactory;
import main.java.com.cafepos.catalog.Product;

import java.math.BigDecimal;

public class OrderManagerGod
{
    // Step 2: public static globals are restrictive
    // different items fall into different tax brackets i.e. Alcohol
    public static int TAX_PERCENT = 10;
    // Step 2: Discount functionality does not need to be a global variable unless handled in multiple classes
    // which by itself is cause for concern
    public static String LAST_DISCOUNT_CODE = null;

    // Step 2: This is a massive method handling far too much.
    // Where variables and methods are independent of other aspects of the code functionality should be extracted to their own classes or methods.
    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt)
    {
        /*
        Step 1:
            Extensive refactoring had to take place before the first step of the lab could occur.
            Code "smell" is code that runs but leaves a lingering feeling of poor implementation or fragility in the code.
            This is the definition used by Kent Beck on WardsWiki in the late 1990s

            The supplied class "OrderManagerGod" had many errors and function calls that just simply didn't exist in the codebase.
            I only fixed these errors and implemented missing functions. I have not altered the functionality at all.
            The testing step could not occur without removing the errors in the code - hence, that is all I have done.

            "Run `mvn -q test`. If tests fail, **do not refactor yet**. If your Money.toString() renders with
            two decimals (as in Weeks 2–5), the string assertions above will pass exactly." - A sentence from the lab sheet
            suggests that the tests should run and either pass or fail, as opposed trigger errors: this is my reason for fixing the errors.
         */
        ProductFactory factory = new ProductFactory();
        Product product = factory.create(recipe);
        Money unitPrice;

        // Step 2: Shouldn't be any need for a try-catch here.
        try
        {
            // Step 2: Reduced readability by inlining import
            // use of var suggests the return type is different between assignments
            // There is none, use concrete declaration for increased readability
            var priced = product instanceof main.java.com.cafepos.decorator.Priced
                    p ? p.price() : product.basePrice();
            unitPrice = priced;
        }
        catch (Exception e)
        {
            unitPrice = product.basePrice();
        }

        // Step 2: Inexplicable handling of values
        if (qty <= 0) qty = 1;

        Money subtotal = unitPrice.multiply(qty);
        Money discount = Money.zero();

        //Step 2: Handling of discounts is complex and relies on a new function converting the Money class amount to a readable variable,
        // This fundamentally defeats the purpose of why we encapsulated functionality into the Money class
        if (discountCode != null)
        {
            if (discountCode.equalsIgnoreCase("LOYAL5"))
            {
                // Step 2: Use of magic numbers. Consider extracting method called "calcDiscountPercent(discount)"
                // subtotal.multiply.divide returns Money. No need to use Money.of()
                discount = Money.of(subtotal.multiply(5).divide(100).asBigDecimal());
            }
            else if (discountCode.equalsIgnoreCase("COUPON1"))
            {
                discount = Money.of(1.00);
            }
            else if (discountCode.equalsIgnoreCase("NONE"))
            {
                discount = Money.zero();
            }
            else
            {
                discount = Money.zero();
            }
            LAST_DISCOUNT_CODE = discountCode;
        }

        // Step 2: A lot of building of new Money classes. This could be handled more elegantly
        Money discounted = Money.of(subtotal.subtract(discount).asBigDecimal());
        // Step 2: (This has been refactored so the code runs, but this is what I understood the original code to be doing)
        // asBigDecimal() does not return a BigDecimal type
        // furthermore, reliance on exposing an explicitly hidden variable needs revision
        // Step 2: This is not intuitively handled.
        if (BigDecimal.valueOf(discounted.asBigDecimal()).signum() < 0) discounted = Money.zero();

        // Step 2: Can be extracted into its own method
        // Also returns type of Money except for the call to "asBigDecimal"
        // Can just merge lines 96 and 97 into a single call.
        var tax = Money.of(discounted.multiply(TAX_PERCENT).divide(100).asBigDecimal());
        var total = discounted.add(tax);

        // Step 2: Extract to its own method
        // Step 2: without a payment type the customer cannot pay
        // function should not run at all, and instead prompt customer to choose a payment type
        if (paymentType != null)
        {
            if (paymentType.equalsIgnoreCase("CASH"))
            {
                System.out.println("[Cash] Customer paid " + total + " EUR");
            }
            else if (paymentType.equalsIgnoreCase("CARD"))
            {
                System.out.println("[Card] Customer paid " + total + " EUR with card ****1234");
            }
            else if (paymentType.equalsIgnoreCase("WALLET"))
            {
                System.out.println("[Wallet] Customer paid " + total + " EUR via wallet user-wallet-789");
            }
            else
            {
                System.out.println("[UnknownPayment] " + total);
            }
        }

        // Step 2: Extract to its own method
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(") x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");

        if (BigDecimal.valueOf(discount.asBigDecimal()).signum() > 0)
        {
            receipt.append("Discount: -").append(discount).append("\n");
        }

        // Step 2: Use a formatted a string and append the string to the stringBuilder in order
        // to increase readability.
        receipt.append("Tax (").append(TAX_PERCENT).append("%): ").append(tax).append("\n");
        receipt.append("Total: ").append(total);

        String out = receipt.toString();

        // Step 2: We don't need to build the receipt if we're not printing it.
        if (printReceipt)
        {
            System.out.println(out);
        }

        return out;
    }
}