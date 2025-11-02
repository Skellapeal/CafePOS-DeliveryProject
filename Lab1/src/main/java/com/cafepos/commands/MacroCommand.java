package main.java.com.cafepos.commands;

public class MacroCommand implements ICommand
{
    private final ICommand[] steps;

    public MacroCommand(ICommand[] steps)
    {
        this.steps = steps;
    }

    @Override
    public void execute()
    {
        for (ICommand command : steps)
        {
            command.execute();
        }
    }

    @Override
    public void undo()
    {
        for (int i = steps.length - 1; i >= 0; i--)
        {
            steps[i].undo();
        }
    }
}