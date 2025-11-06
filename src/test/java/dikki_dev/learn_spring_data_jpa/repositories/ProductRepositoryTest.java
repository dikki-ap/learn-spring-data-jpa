package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import dikki_dev.learn_spring_data_jpa.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateProduct(){
        // Product 1
        Category category1 = categoryRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(category1);

        Product product1 = new Product();
        product1.setName("Samsung S25 Ultra");
        product1.setPrice(25_000L);
        product1.setCategory(category1);
        productRepository.save(product1);

        // Product 2
        Category category28 = categoryRepository.findById(28L).orElse(null);
        Assertions.assertNotNull(category28);

        Product product2 = new Product();
        product2.setName("Uniqlo Airism");
        product2.setPrice(250_000L);
        product2.setCategory(category28);
        productRepository.save(product2);

        // Product 3
        Category category29 = categoryRepository.findById(29L).orElse(null);
        Assertions.assertNotNull(category29);

        Product product3 = new Product();
        product3.setName("Domain Driven Design Book");
        product3.setPrice(33_000L);
        product3.setCategory(category29);
        productRepository.save(product3);
    }

    @Test
    void testFindProductByCategoryName(){
        List<Product> electronicProducts = productRepository.findAllByCategory_Name("Small Electronic");
        Assertions.assertEquals(1, electronicProducts.size());
        Assertions.assertEquals("Samsung S25 Ultra", electronicProducts.get(0).getName());

        List<Product> clothesProducts = productRepository.findAllByCategory_Name("Clothes");
        Assertions.assertEquals(1, clothesProducts.size());
        Assertions.assertEquals("Uniqlo Airism", clothesProducts.get(0).getName());

        List<Product> booksProducts = productRepository.findAllByCategory_Name("Books");
        Assertions.assertEquals(1, booksProducts.size());
        Assertions.assertEquals("Domain Driven Design Book", booksProducts.get(0).getName());
    }

    @Test
    void testFindProductByCategoryNameSortByPrice(){
        Sort sort = Sort.by(Sort.Order.asc("price")); // Membuat filter sort ASC berdasarkan "price"
        List<Product> electronicProducts = productRepository.findAllByCategory_Name("Small Electronic", sort);

        Assertions.assertEquals(2, electronicProducts.size());
        Assertions.assertEquals("Samsung S24 Ultra", electronicProducts.get(0).getName());
        Assertions.assertEquals("Samsung S25 Ultra", electronicProducts.get(1).getName());
    }
}
