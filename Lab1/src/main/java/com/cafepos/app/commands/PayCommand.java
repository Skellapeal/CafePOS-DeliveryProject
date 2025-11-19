package main.java.com.cafepos.app.commands;

import main.java.com.cafepos.domain.payment.PaymentStrategy;

public class PayCommand implements ICommand
{
    private final OrderService service;
    private final PaymentStrategy strategy;
    private final int taxPercent;

    public PayCommand(OrderService service, PaymentStrategy strategy, int taxPercent)
    {
        this.service = service;
        this.strategy = strategy;
        this.taxPercent = taxPercent;
    }

    @Override
    public void execute()
    {
        service.pay(strategy, taxPercent);
    }
}