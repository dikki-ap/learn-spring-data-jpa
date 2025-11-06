package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Product memiliki relasi ke Category (1 Category to M Product)
    // Jika ada hubungan relasi, dan ingin mengakses field "Category" dengan file "name" bisa gunakan tanda "_" (underscore)
    // SECARA DEFAULT BERSIFAT (EQUALS) jika tidak menyebutkan Like / Equals
    /*
        SELECT p.*
        FROM products p
        JOIN categories c ON p.category_id = c.id
        WHERE c.name = ?;
     */
    List<Product> findAllByCategory_Name(String name);
    List<Product> findAllByCategory_Name(String name, Sort sort); // Menambahkan "Sort" di parameter terakhir agar bisa Sorting
    List<Product> findAllByCategory_Name(String name, Pageable pageable); // Menambahkan Pagination, sudah include juga dengan Sorting
}
