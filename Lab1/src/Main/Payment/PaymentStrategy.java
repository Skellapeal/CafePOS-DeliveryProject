package Main.Payment;

import Main.Domain.Order;

public interface PaymentStrategy
{
    void pay(Order order);
}
