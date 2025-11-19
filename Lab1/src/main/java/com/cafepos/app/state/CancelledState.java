package main.java.com.cafepos.app.state;

import main.java.com.cafepos.ui.ConsoleView;

final class CancelledState implements State
{
    @Override public void pay(OrderFSM ctx)
    {
        ConsoleView.print("[State] Cancelled");
    }
    @Override public void prepare(OrderFSM ctx)
    {
        ConsoleView.print("[State] Cancelled");
    }
    @Override public void markReady(OrderFSM ctx)
    {
        ConsoleView.print("[State] Cancelled");
    }
    @Override public void deliver(OrderFSM ctx)
    {
        ConsoleView.print("[State] Cancelled");
    }
    @Override public void cancel(OrderFSM ctx)
    {
        ConsoleView.print("[State] Already cancelled");
    }
    @Override public String name()
    {
        return "CANCELLED";
    }
}
