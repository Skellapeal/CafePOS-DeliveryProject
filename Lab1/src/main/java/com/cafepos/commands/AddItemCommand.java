package main.java.com.cafepos.commands;

public final class AddItemCommand implements ICommand
{
    private final OrderService orderService;
    private final String recipe;
    private final int quantity;

    public AddItemCommand(OrderService service, String recipe, int qty)
    {
        orderService = service;
        this.recipe = recipe;
        this.quantity = qty;
    }

    @Override
    public void execute()
    {
        orderService.addItem(recipe, quantity);
    }

    @Override
    public void undo()
    {
        orderService.removeLastItem();
    }
}