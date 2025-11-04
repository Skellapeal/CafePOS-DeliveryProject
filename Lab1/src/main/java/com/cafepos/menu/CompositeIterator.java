package main.java.com.cafepos.menu;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CompositeIterator implements Iterator<MenuComponent>
{
    private final Deque<Iterator<MenuComponent>> stack = new ArrayDeque<>();
    public CompositeIterator(Iterator<MenuComponent> root)
    {
        stack.push(root);
    }

    @Override
    public boolean hasNext()
    {
        while(!stack.isEmpty())
        {
            if (stack.peek().hasNext()) return true;
            stack.pop();
        }
        return false;
    }

    @Override
    public MenuComponent next()
    {
        if(!hasNext()) throw new NoSuchElementException();
        Iterator<MenuComponent> iterator = stack.peek();
        MenuComponent menuComponent = iterator != null ? iterator.next() : null;
        if(menuComponent instanceof Menu menu)
        {
            stack.push(menu.childrenIterator());
        }
        return menuComponent;
    }
}
