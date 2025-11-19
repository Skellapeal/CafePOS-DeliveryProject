package main.java.com.cafepos.app.state;

import main.java.com.cafepos.ui.ConsoleView;

final class PreparingState implements State
{
    @Override public void pay(OrderFSM ctx)
    {
        ConsoleView.print("[State] Already paid");
    }
    @Override public void prepare(OrderFSM ctx)
    {
        ConsoleView.print("[State] Still preparing...");
    }
    @Override public void markReady(OrderFSM ctx)
    {
        ConsoleView.print("[State] Ready for pickup");
        ctx.set(new ReadyState());
    }
    @Override public void deliver(OrderFSM ctx)
    {
        ConsoleView.print("[State] Deliver not allowed before ready");
    }
    @Override public void cancel(OrderFSM ctx)
    {
        ConsoleView.print("[State] Cancelled during prep");
        ctx.set(new CancelledState());
    }
    @Override public String name()
    {
        return "PREPARING";
    }
}

