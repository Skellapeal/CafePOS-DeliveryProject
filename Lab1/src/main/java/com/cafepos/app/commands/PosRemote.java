package main.java.com.cafepos.app.commands;

import java.util.ArrayDeque;
import java.util.Deque;

public class PosRemote
{
    private final ICommand[] slots;
    private final Deque<ICommand> history = new ArrayDeque<>();
    public PosRemote(int n) { this.slots = new ICommand[n]; }
    public void setSlot(int i, ICommand command) { slots[i] = command; }

    public void press(int i)
    {
        ICommand command = slots[i];
        if (command != null)
        {
            command.execute();
            history.push(command);
        }
        else
        {
            System.out.println("[Remote] No command in slot " + i);
        }
    }

    public void undo()
    {
        if (history.isEmpty())
        {
            System.out.println("[Remote] Nothing to undo");
            return;
        }

        history.pop().undo();
    }
}