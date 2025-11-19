package main.java.com.cafepos.app.state;

public interface State
{
    void pay(OrderFSM context);
    void prepare(OrderFSM context);
    void markReady(OrderFSM context);
    void deliver(OrderFSM context);
    void cancel(OrderFSM context);
    String name();
}
