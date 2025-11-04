package main.java.com.cafepos.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Menu extends MenuComponent
{
    private final String name;
    private final List<MenuComponent> children = new ArrayList<>();

    public Menu(final String name)
    {
        if(name == null || name.isBlank())
        {
            throw new  IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    @Override
    public void add (MenuComponent menuComponent)
    {
        children.add(menuComponent);
    }

    @Override
    public void remove (MenuComponent menuComponent)
    {
        children.remove(menuComponent);
    }

    @Override
    public MenuComponent getChild(int i)
    {
        return children.get(i);
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public Iterator<MenuComponent> iterator()
    {
        return new CompositeIterator(childrenIterator());
    }

    public Iterator<MenuComponent> childrenIterator()
    {
        return children.iterator();
    }

    @Override
    public void print()
    {
        System.out.println(name);
        for (MenuComponent menuComponent : children)
        {
            menuComponent.print();
        }
    }

    public List<MenuComponent> allItems()
    {
        List<MenuComponent> out = new ArrayList<>();
        Iterator<MenuComponent> iterator = iterator();
        while (iterator.hasNext())
        {
            out.add(iterator.next());
        }
        return out;
    }

    public List<MenuItem> vegetarianItems()
    {
        return allItems().stream()
                .filter(menuComponent -> menuComponent instanceof MenuItem menuItem && menuItem.vegetarian())
                .map(menuComponent -> (MenuItem) menuComponent)
                .collect(Collectors.toList());
    }
}