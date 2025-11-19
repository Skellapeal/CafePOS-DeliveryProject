package main.java.com.cafepos.app.state;

import main.java.com.cafepos.ui.ConsoleView;

final class DeliveredState implements State
{
    @Override public void pay(OrderFSM ctx)
    {
        ConsoleView.print("[State] Completed");
    }
    @Override public void prepare(OrderFSM ctx)
    {
        ConsoleView.print("[State] Completed");
    }
    @Override public void markReady(OrderFSM ctx)
    {
        ConsoleView.print("[State] Completed");
    }
    @Override public void deliver(OrderFSM ctx)
    {
        ConsoleView.print("[State] Already delivered");
    }
    @Override public void cancel(OrderFSM ctx)
    {
        ConsoleView.print("[State] Completed");
    }
    @Override public String name()
    {
        return "DELIVERED";
    }
}
