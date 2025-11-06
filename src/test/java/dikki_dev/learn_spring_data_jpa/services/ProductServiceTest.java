package dikki_dev.learn_spring_data_jpa.services;

import dikki_dev.learn_spring_data_jpa.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void testSearch() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> result = productService.search("S23", "Small Electronic", 50000L, 100000L, pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.stream().allMatch(p ->
                p.getName().contains("S23") &&
                        p.getCategory().getName().equals("Small Electronic") &&
                        p.getPrice() >= 50000 &&
                        p.getPrice() <= 100000
        ));
    }
}
