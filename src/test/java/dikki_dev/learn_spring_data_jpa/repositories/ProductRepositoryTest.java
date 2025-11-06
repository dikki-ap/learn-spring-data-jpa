package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import dikki_dev.learn_spring_data_jpa.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Sort multiSort = Sort.by(Sort.Order.asc("price"), Sort.Order.asc("id")); // Multi Sorting
        List<Product> electronicProducts = productRepository.findAllByCategory_Name("Small Electronic", sort);

        Assertions.assertEquals(2, electronicProducts.size());
        Assertions.assertEquals("Samsung S24 Ultra", electronicProducts.get(0).getName());
        Assertions.assertEquals("Samsung S25 Ultra", electronicProducts.get(1).getName());
    }

    @Test
    void testFindProductByCategoryNameSortByPagination(){
        // PageIndex 0, PageSize 2 (Total Data 5)
        Pageable paginationConfig = PageRequest.of(
                0,
                2,
                Sort.by(
                        Sort.Order.asc("price"),
                        Sort.Order.asc("id")
                )
        );

        List<Product> electronicProductsPage0 = productRepository.findAllByCategory_Name("Small Electronic", paginationConfig);

        Assertions.assertEquals(2, electronicProductsPage0.size());
        Assertions.assertEquals("Samsung S24 Ultra", electronicProductsPage0.get(0).getName());
        Assertions.assertEquals("Samsung S25 Ultra", electronicProductsPage0.get(1).getName());

        // PageIndex 1, PageSize 2 (Total Data 5)
        paginationConfig = PageRequest.of(
                1,
                2,
                Sort.by(
                        Sort.Order.asc("price"),
                        Sort.Order.asc("id")
                )
        );
        List<Product> electronicProductsPage1 = productRepository.findAllByCategory_Name("Small Electronic", paginationConfig);

        Assertions.assertEquals(2, electronicProductsPage1.size());
        Assertions.assertEquals("Samsung S23", electronicProductsPage1.get(0).getName());
        Assertions.assertEquals("Samsung S23 Plus", electronicProductsPage1.get(1).getName());

        // PageIndex 2, PageSize 2 (Total Data 5)
        paginationConfig = PageRequest.of(
                2,
                2,
                Sort.by(
                        Sort.Order.asc("price"),
                        Sort.Order.asc("id")
                )
        );
        List<Product> electronicProductsPage2 = productRepository.findAllByCategory_Name("Small Electronic", paginationConfig);

        Assertions.assertEquals(1, electronicProductsPage2.size());
        Assertions.assertEquals("Samsung S23 Ultra", electronicProductsPage2.get(0).getName());
    }
}
