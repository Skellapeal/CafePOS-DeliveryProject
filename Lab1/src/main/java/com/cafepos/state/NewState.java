package main.java.com.cafepos.state;

final class NewState implements State
{
    @Override public void pay(OrderFSM context)
    {
        System.out.println("[State] Paid → Preparing");
        context.set(new PreparingState());
    }
    @Override public void prepare(OrderFSM context)
    {
        System.out.println("[State] Cannot prepare before pay");
    }
    @Override public void markReady(OrderFSM context)
    {
        System.out.println("[State] Not ready yet");
    }
    @Override public void deliver(OrderFSM context)
    {
        System.out.println("[State] Cannot deliver yet");
    }
    @Override public void cancel(OrderFSM context)
    {
        System.out.println("[State] Cancelled");
        context.set(new CancelledState());
    }

    @Override public String name()
    {
        return "NEW";
    }
}
