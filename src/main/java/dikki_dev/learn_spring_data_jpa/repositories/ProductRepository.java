package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
//    List<Product> findAllByCategory_Name(String name, Pageable pageable); // Menambahkan Pagination, sudah include juga dengan Sorting
    Page<Product> findAllByCategory_Name(String name, Pageable pageable); // MENGGANTI TIPE DATA "Page<T>", agar bisa mendapatkan informasi total data dan total Page

    @Transactional // Khusus untuk operation YANG TIDAK READ ONLY, tambahkan @Transactional secara mandiri, karena JpaRepository secara default @Transactional(readonly = true)
    int deleteByName(String name); // Membuat Method "DELETE" berdasarkan "name" dan mengembalikan integer untuk melihat berapa record affected

    // Buat JPA Repository dengan nama yang sama seperti "NamedQuery" agar bisa menggunakan hal tersebut
    List<Product> searchProductUsingName(@Param("name") String name);

    // Membuat method dengan "@Query" Annotation dengan JPA QL
    // Di "@Query" Annotation property "nativeQuery" defaultnya false, yang artinya akan menggunakan JPA QL
    // Jika nativeQuery = true maka bisa menggunakan QUERY DB yang asli
    @Query(value = "select p from Product p where p.name like :name or p.category.name like :name")
    List<Product> searchProductByName(@Param("name")String name);

    /*
        -- @Modifying Annotation --
        - Berfungsi untuk "@Query" Annotation yang NOT READ ONLY (UPDATE / DELETE)
        - Wajib menambahkan "@Modifying" agar Spring tau bahwa ini NOT READ ONLY
     */
    @Modifying
    @Query(value = "delete from Product p where p.name = :name")
    int deleteProductUsingName(@Param("name") String name);

    @Modifying
    @Query(value = "update from Product p set p.price = 0 where p.id = :id")
    int updateProductPriceToZero(@Param("price") Long id);
}
