package Main.Commands;

public interface ICommand
{
    void execute(String[] args, CommandContext context);
}
