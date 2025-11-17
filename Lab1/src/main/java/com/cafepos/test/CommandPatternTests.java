package main.java.com.cafepos.test;

import main.java.com.cafepos.commands.AddItemCommand;
import main.java.com.cafepos.commands.MacroCommand;
import main.java.com.cafepos.commands.OrderService;
import main.java.com.cafepos.commands.PosRemote;
import main.java.com.cafepos.factory.ProductFactory;
import main.java.com.cafepos.infra.LegacyThermalPrinter;
import main.java.com.cafepos.domain.LineItem;
import main.java.com.cafepos.domain.Order;
import main.java.com.cafepos.domain.OrderIds;
import main.java.com.cafepos.infra.LegacyPrinterAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandPatternTests
{
    Order myOrder;
    OrderService orderService;
    @BeforeEach
    void setUp()
    {
        myOrder = new Order(OrderIds.next());
        orderService = new OrderService(myOrder);
    }

    @Test
    void undoStackTest()
    {
        AddItemCommand addItem = new AddItemCommand(orderService, "ESP", 1);
        addItem.execute();
        assertEquals(1, myOrder.items().size());
        addItem.undo();
        assertEquals(0, myOrder.items().size());
    }

    @Test
    void reverseOrderUndo()
    {
        ProductFactory productFactory = new ProductFactory();
        AddItemCommand addItem = new AddItemCommand(orderService, "ESP", 1);
        AddItemCommand addItem2 = new AddItemCommand(orderService, "LAT", 1);

        MacroCommand allCommands = new MacroCommand(new AddItemCommand[]{addItem, addItem2});
        allCommands.execute();
        assertEquals(2, myOrder.items().size());
        addItem.undo();
        assertEquals(1, myOrder.items().size());
        assertEquals(new LineItem(productFactory.create("ESP"), 1).product().name(), myOrder.items().getFirst().product().name());
    }

    @Test
    void legacyPrinterTest()
    {
        LegacyThermalPrinter printer = new LegacyThermalPrinter();
        LegacyPrinterAdapter printerAdapter = new LegacyPrinterAdapter(printer);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        printerAdapter.print("ABC");

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("3"));
    }

    @Test
    void smallIntegrationTest()
    {
        PosRemote keyBindings = new PosRemote(2);
        AddItemCommand addItem = new AddItemCommand(orderService, "ESP", 1);
        AddItemCommand addItem2 = new AddItemCommand(orderService, "LAT", 1);

        keyBindings.setSlot(0, addItem);
        keyBindings.setSlot(1, addItem2);

        for(int i = 0; i <= 1; i++)
        {
            keyBindings.press(i);
        }

        assertEquals("5.70", myOrder.subtotal().toString());
    }
}
