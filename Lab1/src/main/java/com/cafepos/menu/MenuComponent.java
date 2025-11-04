package main.java.com.cafepos.menu;

import main.java.com.cafepos.common.Money;

import java.util.Iterator;

public abstract class MenuComponent
{
    public void add(MenuComponent menuComponent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void remove(MenuComponent menuComponent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MenuComponent getChild(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String name()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Money price()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean vegetarian()
    {
        return false;
    }

    public Iterator<MenuComponent> iterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void print()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
