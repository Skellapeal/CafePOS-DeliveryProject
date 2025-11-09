package main.java.com.cafepos.test;

import main.java.com.cafepos.common.Money;
import main.java.com.cafepos.menu.Menu;
import main.java.com.cafepos.menu.MenuComponent;
import main.java.com.cafepos.menu.MenuItem;
import main.java.com.cafepos.state.OrderFSM;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Week9Tests
{
    @Test
    void depth_first_iteration_collects_all_nodes()
    {
        Menu root = new Menu("ROOT");
        Menu a = new Menu("A");
        Menu b = new Menu("B");

        root.add(a); root.add(b);

        a.add(new MenuItem("x", Money.of(1.0), true));
        b.add(new MenuItem("y", Money.of(2.0), false));

        List<String> names = root.allItems().stream()
                            .map(MenuComponent::name)
                            .toList();

        assertTrue(names.contains("x"));
        assertTrue(names.contains("y"));
    }
    @Test
    void vegetarianItems_returns_only_veg_items()
    {
        Menu root = new Menu("ROOT");
        Menu desserts = new Menu("Desserts");
        root.add(desserts);

        desserts.add(new MenuItem("Cheesecake", Money.of(3.50), false));
        desserts.add(new MenuItem("Oat Cookie", Money.of(1.20), true));

        List<MenuItem> veg = root.vegetarianItems();

        assertEquals(1, veg.size());
        assertEquals("Oat Cookie", veg.getFirst().name());
        assertTrue(veg.getFirst().vegetarian());
    }
    @Test
    void order_fsm_happy_path()
    {
        OrderFSM fsm = new OrderFSM();

        assertEquals("NEW", fsm.status());
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
        fsm.markReady();
        assertEquals("READY", fsm.status());
        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());
    }
    @Test
    void order_fsm_cancel_path()
    {
        OrderFSM fsm = new OrderFSM();

        assertEquals("NEW", fsm.status());
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }

    @Test
    void select_menu_item_and_complete_order_lifecycle()
    {
        // === Build a sample menu ===
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");

        root.add(drinks);

        drinks.add(new MenuItem("Latte (Large)", Money.of(3.90), true));
        drinks.add(new MenuItem("Espresso", Money.of(2.50), true));

        // === Select an item by name ===
        MenuItem selected = root.allItems().stream()
                .filter(menuComponent -> menuComponent instanceof MenuItem menuItem && menuItem.name().equals("Latte (Large)"))
                .map(menuComponent -> (MenuItem) menuComponent)
                .findFirst()
                .orElseThrow();

        assertEquals("Latte (Large)", selected.name());
        assertEquals(Money.of(3.90), selected.price());

        // === Create an order FSM and progress through states ===
        OrderFSM order = new OrderFSM();
        assertEquals("NEW", order.status());

        order.pay();
        assertEquals("PREPARING", order.status());

        order.markReady();
        assertEquals("READY", order.status());

        order.deliver();
        assertEquals("DELIVERED", order.status());

        // === Verify final outcome ===
        System.out.printf("Order for %s completed, total = %s%n", selected.name(), selected.price());
        assertEquals("DELIVERED", order.status());
    }
}

