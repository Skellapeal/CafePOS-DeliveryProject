package main.java.com.cafepos.payment;

import main.java.com.cafepos.order.Order;

public class CardPayment implements PaymentStrategy
{
    private final String cardNumber;
    public CardPayment (String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(Order order)
    {
        System.out.println("[Card] Customer paid " + order.totalWithTax(10) + " EUR with card " + maskCardNumber() );
    }

    private String maskCardNumber()
    {
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}