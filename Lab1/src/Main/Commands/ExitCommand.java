package Main.Commands;

public class ExitCommand implements ICommand
{
    @Override
    public void execute(String[] args, CommandContext context)
    {
        System.out.println("\nThank you for visiting StarBuzz Café!");
        System.out.println("Have a great day!");
    }
}