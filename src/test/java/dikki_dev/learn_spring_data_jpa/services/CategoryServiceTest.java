
package dikki_dev.learn_spring_data_jpa.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testTransactionalSuccess(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            // Jika menjalankan method ini, harusnya akan ada Exception
            // Karena adanya @Transactional, dan otomatis Rollback tanpa menambah data ke DB
           categoryService.create();
        });
    }

    @Test
    void testTransactionalFailed(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            // Jika menjalankan method ini, memangg ada Exception juga, tetapi data telah masuk ke DB terlebih dahulu
            // @Transactional tidak berfungsi karena dipanggil di dalam object / class yang sama
            categoryService.testCreate();
        });
    }

    @Test
    void testProgrammaticTransactionWithTransactionOperations(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            // Menjalankan Transactional di proses ASYNC dengan Transaction Operations
            categoryService.createWithTransactionOperations();
        });
    }

    @Test
    void testProgrammaticTransactionWithPlatformTransactionManager(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            // Menjalankan Transactional di proses ASYNC dengan Transaction Operations
            categoryService.createWithPlatformTransactionManager();
        });
    }
}
