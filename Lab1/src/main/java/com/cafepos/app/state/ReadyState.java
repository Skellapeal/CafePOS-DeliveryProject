package main.java.com.cafepos.app.state;

import main.java.com.cafepos.ui.ConsoleView;

final class ReadyState implements State
{
    @Override public void pay(OrderFSM ctx)
    {
        ConsoleView.print("[State] Already paid");
    }

    @Override public void prepare(OrderFSM ctx)
    {
        ConsoleView.print("[State] Already prepared");
    }

    @Override public void markReady(OrderFSM ctx)
    {
        ConsoleView.print("[State] Already ready");
    }

    @Override public void deliver(OrderFSM ctx)
    {
        ConsoleView.print("[State] Delivered");
        ctx.set(new DeliveredState());
    }

    @Override public void cancel(OrderFSM ctx)
    {
        ConsoleView.print("[State] Cannot cancel after ready");
    }

    @Override public String name()
    {
        return "READY";
    }
}