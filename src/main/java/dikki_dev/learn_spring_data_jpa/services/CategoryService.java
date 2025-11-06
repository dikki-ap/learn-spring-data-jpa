package dikki_dev.learn_spring_data_jpa.services;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import dikki_dev.learn_spring_data_jpa.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /*
        -- @Transactional Annotation --
        - Bisa disebut juga dengan "Declarative Transaction"
        - Tidak perlu membuat "Begin" dan "Commit" secara manual

        RULES:
        1. Harus digunakan di object / class yang berbeda
        - Misalnya ini dibuat di CategoryService.create(), harus dipanggil di luar object CategoryService agar @Transactional berjalan
        - Jika dipanggil di object yang sama, tidak akan berfungsi, karena @Transactional itu dibungkus oleh AOP seperti proxy
     */
    @Transactional
    public void create(){
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category-" + i);
            categoryRepository.save(category);
        }

        throw new RuntimeException("Ups, Rollback please!");
    }

    public void testCreate(){
        create();
    }
}
