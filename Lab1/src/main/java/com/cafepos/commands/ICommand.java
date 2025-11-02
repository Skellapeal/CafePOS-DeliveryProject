package main.java.com.cafepos.commands;

public interface ICommand
{
    void execute();
    default void undo() { }
}
