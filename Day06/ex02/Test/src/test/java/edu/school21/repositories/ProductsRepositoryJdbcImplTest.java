package edu.school21.repositories;

import edu.school21.models.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsRepositoryJdbcImplTest {
    private EmbeddedDatabase goodDb;
    private EmbeddedDatabase wrongDb;
    private ProductsRepositoryJdbcImpl goodProductsRepository;
    private ProductsRepositoryJdbcImpl wrongProductsRepository;

    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = Arrays.asList(
            new Product(0, "pr1", 50),
            new Product(1, "pr2", 100),
            new Product(2, "pr3", 500),
            new Product(3, "pr4", 5000),
            new Product(4, "pr5", 10000)
    );

    @BeforeEach
    public void init(){
        goodDb = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("Day06")
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        wrongDb = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("KJHGEW54UHLFJVKW4580IGJERLWKJWEM")
                .build();

        goodProductsRepository = new ProductsRepositoryJdbcImpl(goodDb);
        wrongProductsRepository = new ProductsRepositoryJdbcImpl(wrongDb);
    }

    @AfterEach
    public void destroy(){
        goodDb.shutdown();
    }

    @Test
    public void testFindAll(){
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, goodProductsRepository.findAll());
        assertNotNull(goodProductsRepository.findAll());
        assertNull(wrongProductsRepository.findAll());
    }

    @Test
    public void testFindById(){
        assertFalse(goodProductsRepository.findById(123L).isPresent());
        Optional<Product> found = goodProductsRepository.findById(4L);
        if (!found.isPresent()) assertTrue(false);
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS.get(4), found.get());
    }

    @Test
    public void testUpdate(){
        {
            goodProductsRepository.update(new Product(4L, "Product5", 999));
            Optional<Product> found = goodProductsRepository.findById(4L);
            if (!found.isPresent()) assertTrue(false);
            Product product = found.get();
            assertEquals(product.getId(), 4L);
            assertEquals(product.getName(), "Product5");
            assertEquals(product.getPrice(), 999);
        }
        {
            try {
                wrongProductsRepository.update(new Product(123L, "Product5", 999));
                assertNotNull(null);
            } catch (RuntimeException e){
                assertTrue(true);
            }
        }
    }

    @Test
    public void testSave(){
        {
            goodProductsRepository.save(new Product(5L, "pr6", 1234));
            Optional<Product> found = goodProductsRepository.findById(5L);
            if (!found.isPresent()) assertTrue(false);
            Product product = found.get();
            assertEquals(product.getId(), 5L);
            assertEquals(product.getName(), "pr6");
            assertEquals(product.getPrice(), 1234);
        }
        {
            try{
                wrongDb.shutdown();
                wrongProductsRepository.save(new Product(5L, "pr6", 1234));
                assertNotNull(null);
            } catch (RuntimeException e) {
                assertTrue(true);
            }
        }
    }

    @Test
    public void testDelete(){
        {
            Optional<Product> found = goodProductsRepository.findById(4L);
            if (!found.isPresent()) assertTrue(false);
            goodProductsRepository.delete(4L);
            found = goodProductsRepository.findById(4L);
            if (found.isPresent()) assertTrue(false);
        }
        {
            try{
                wrongDb.shutdown();
                wrongProductsRepository.delete(2222L);
                assertNotNull(null);
            } catch (RuntimeException e){
                assertTrue(false);
            }
        }
    }

    /*
    @BeforeEach
    public void init(){
        try {
            database = new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.HSQL)
                    .setName("Day06")
                    .addScript("schema.sql")
                    .addScript("data.sql")
                    .build();
            productsRepository = new ProductsRepositoryJdbcImpl(database.getConnection());
        } catch (SQLException e) {
            assertNotNull(null);
        }
    }

    @AfterEach
    public void destroy(){
        database.shutdown();
    }

    @Test
    public void testFindAll(){
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, productsRepository.findAll());
        assertNull(productsRepository.findAll());
    }

    @Test
    public void testFindById(){
        assertFalse(productsRepository.findById(123L).isPresent());
        Optional<Product> found = productsRepository.findById(4L);
        if (!found.isPresent()) assertTrue(false);
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS.get(4), found.get());
    }

    @Test
    public void testUpdate(){
        productsRepository.update(new Product(4L, "Product5", 999));
        Optional<Product> found = productsRepository.findById(4L);
        if (!found.isPresent()) assertTrue(false);
        Product product = found.get();
        assertEquals(product.getId(), 4L);
        assertEquals(product.getName(), "Product5");
        assertEquals(product.getPrice(), 999);
    }

    @Test
    public void testSave(){
        productsRepository.save(new Product(5L, "pr6", 1234));
        Optional<Product> found = productsRepository.findById(5L);
        if (!found.isPresent()) assertTrue(false);
        Product product = found.get();
        assertEquals(product.getId(), 5L);
        assertEquals(product.getName(), "pr6");
        assertEquals(product.getPrice(), 1234);
    }

    @Test
    public void testDelete(){
        Optional<Product> found = productsRepository.findById(4L);
        if (!found.isPresent()) assertTrue(false);
        productsRepository.delete(4L);
        found = productsRepository.findById(4L);
        if (found.isPresent()) assertTrue(false);
    }
    */
}
