package main.java.com.cafepos.domain.payment;

import main.java.com.cafepos.domain.Order;

public interface PaymentStrategy
{
    void pay(Order order);
}
