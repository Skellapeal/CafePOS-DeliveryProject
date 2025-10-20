package main.java.com.cafepos.commands;

public interface ICommand
{
    void execute(String[] args, CommandContext context);
}
