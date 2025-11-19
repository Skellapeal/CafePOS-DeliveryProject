package main.java.com.cafepos.demo;

import main.java.com.cafepos.app.state.OrderFSM;

public final class Week9Demo_States
{
    public static void main(String[] args)
    {
        OrderFSM fsm = new OrderFSM();

        System.out.println("Status = " + fsm.status());

        // invalid before pay
        fsm.prepare();
        // NEW -> PREPARING
        fsm.pay();
        // still preparing
        fsm.prepare();
        // PREPARING -> READY
        fsm.markReady();
        // READY -> DELIVERED
        fsm.deliver();

        System.out.println("Status = " + fsm.status());
    }
}
