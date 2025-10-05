package Test;

import Main.Catalog.InMemoryCatalog;
import Main.Catalog.Product;
import Main.Catalog.SimpleProduct;
import Main.Common.Money;

import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryCatalogTest
{
    @Test
    public void testAddAndFindProduct()
    {
        InMemoryCatalog catalog = new InMemoryCatalog();
        SimpleProduct product = new SimpleProduct("P-001", "Test Product", Money.of(10.00));

        catalog.add(product);
        Optional<Product> found = catalog.findById("P-001");

        assertTrue(found.isPresent());
        assertEquals("P-001", found.get().id());
        assertEquals("Test Product", found.get().name());
    }

    @Test
    public void testFindNonExistentProduct()
    {
        InMemoryCatalog catalog = new InMemoryCatalog();
        Optional<Product> found = catalog.findById("NON-EXISTENT");
        assertFalse(found.isPresent());
    }

    @Test
    public void ExceptionCatalogAddNullProductTest()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            InMemoryCatalog catalog = new InMemoryCatalog();
            catalog.add(null);
        });
    }

    @Test
    public void testAddMultipleProducts()
    {
        InMemoryCatalog catalog = new InMemoryCatalog();
        SimpleProduct product1 = new SimpleProduct("P-001", "Product 1", Money.of(5.00));
        SimpleProduct product2 = new SimpleProduct("P-002", "Product 2", Money.of(15.00));

        catalog.add(product1);
        catalog.add(product2);

        assertTrue(catalog.findById("P-001").isPresent());
        assertTrue(catalog.findById("P-002").isPresent());
        assertEquals("Product 1", catalog.findById("P-001").get().name());
        assertEquals("Product 2", catalog.findById("P-002").get().name());
    }

    @Test
    public void testReplaceExistingProduct()
    {
        InMemoryCatalog catalog = new InMemoryCatalog();
        SimpleProduct originalProduct = new SimpleProduct("P-001", "Original", Money.of(5.00));
        SimpleProduct replacementProduct = new SimpleProduct("P-001", "Replacement", Money.of(10.00));

        catalog.add(originalProduct);
        catalog.add(replacementProduct); // Should replace the original

        Optional<Product> found = catalog.findById("P-001");
        assertTrue(found.isPresent());
        assertEquals("Replacement", found.get().name());
        assertEquals("10.00", found.get().basePrice().toString());
    }

    @Test
    public void testFindByIdWithNullId()
    {
        InMemoryCatalog catalog = new InMemoryCatalog();
        Optional<Product> found = catalog.findById(null);
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByIdWithEmptyString()
    {
        InMemoryCatalog catalog = new InMemoryCatalog();
        Optional<Product> found = catalog.findById("");
        assertFalse(found.isPresent());
    }

    @Test
    public void testCatalogPersistenceAcrossOperations()
    {
        InMemoryCatalog catalog = new InMemoryCatalog();
        SimpleProduct product = new SimpleProduct("P-PERSIST", "Persistent Product", Money.of(20.00));

        catalog.add(product);

        Optional<Product> found1 = catalog.findById("P-PERSIST");
        Optional<Product> found2 = catalog.findById("P-PERSIST");

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertEquals(found1.get().id(), found2.get().id());
    }
}