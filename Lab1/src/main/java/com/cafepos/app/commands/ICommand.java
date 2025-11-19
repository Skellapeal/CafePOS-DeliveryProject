package main.java.com.cafepos.app.commands;

public interface ICommand
{
    void execute();
    default void undo() { }
}
