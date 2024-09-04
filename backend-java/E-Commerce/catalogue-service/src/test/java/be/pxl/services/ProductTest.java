package be.pxl.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest{

    @Test
    void testProductInitialization(){
        Long id = 1L;
        String name = "TestProduct";
        String desc = "TestDescription";
        String categoryName = "testCategory";
        int stock = 100;
        double price = 10;
        String supplier = "TestSupplier";
        int wishlisted = 2;

        Category category = new Category(categoryName);

        Product product = new Product(id, name, desc, category, stock, price, supplier, wishlisted);

        assertEquals(id, product.getId());
        assertEquals(name, product.getProductName());
        assertEquals(desc, product.getDescription());
        assertEquals(categoryName, product.getCategory().getCategoryName());
        assertEquals(stock, product.getStock());
        assertEquals(price, product.getPrice());
        assertEquals(supplier, product.getSupplier());
        assertEquals(wishlisted, product.getWishlisted());
    }

    @Test
    void testDefaultProductInitialization(){
        Product product = new Product();

        assertNull(product.getProductName());
        assertNull(product.getDescription());
        assertNull(product.getCategory());
        assertEquals(0, product.getStock());
        assertEquals(0.0, product.getPrice());
        assertNull(product.getSupplier());
        assertEquals(0, product.getWishlisted());
    }
}