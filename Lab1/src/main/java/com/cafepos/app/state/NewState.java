package main.java.com.cafepos.app.state;

import main.java.com.cafepos.ui.ConsoleView;

final class NewState implements State
{
    @Override public void pay(OrderFSM context)
    {
        ConsoleView.print("[State] Paid → Preparing");
        context.set(new PreparingState());
    }
    @Override public void prepare(OrderFSM context)
    {
        ConsoleView.print("[State] Cannot prepare before pay");
    }
    @Override public void markReady(OrderFSM context)
    {
        ConsoleView.print("[State] Not ready yet");
    }
    @Override public void deliver(OrderFSM context)
    {
        ConsoleView.print("[State] Cannot deliver yet");
    }
    @Override public void cancel(OrderFSM context)
    {
        ConsoleView.print("[State] Cancelled");
        context.set(new CancelledState());
    }

    @Override public String name()
    {
        return "NEW";
    }
}
