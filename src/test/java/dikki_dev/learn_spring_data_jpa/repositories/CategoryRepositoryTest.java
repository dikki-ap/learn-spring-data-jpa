package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateCategory(){
        Category category = new Category();
        category.setName("Books"); // Id tidak perlu dibuat karena auto generated

        // Method "save(entity)" berfungsi untuk CREATE dan UPDATE
        categoryRepository.save(category); // Langsung save ke Database
        Assertions.assertNotNull(category.getId());
    }

    @Test
    void testUpdateCategory(){
        Category category = categoryRepository.findById(1L).orElse(null); // GET ID 1, jika tidak dapat return null
        category.setName("Small Electronic"); // Ubah existing "name" data nya

        // Method "save(entity)" berfungsi untuk CREATE dan UPDATE
        categoryRepository.save(category); // Update, dan save ke Database
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals("Small Electronic", category.getName());
    }

    @Test
    void testQueryMethodFindFirstByNameEquals(){
        Optional<Category> category = categoryRepository.findFirstByNameEquals("Small Electronic");
        Assertions.assertNotNull(category.get().getId());
        Assertions.assertEquals(1, category.get().getId());
    }

    @Test
    void testQueryMethodFindFirstByNameLike(){
        List<Category> category = categoryRepository.findAllByNameLike("%Category%");
        Assertions.assertNotNull(category);
        Assertions.assertEquals("Category-0", category.getFirst().getName());
    }
}
