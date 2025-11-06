package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import dikki_dev.learn_spring_data_jpa.entities.Product;
import dikki_dev.learn_spring_data_jpa.interfaces.SimpleProduct;
import dikki_dev.learn_spring_data_jpa.records.SimpleProductRecord;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
// Menambahkan "JpaSpecificationExecutor<T>" untuk membuat JPA Query secara Advance untuk menggunakan "Specification<T>"
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

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
//    @Modifying
//    @Query(value = "delete from Product p where p.name = :name")
//    int deleteProductUsingName(@Param("name") String name);
//
//    @Modifying
//    @Query(value = "update from Product p set p.price = 0 where p.id = :id")
//    int updateProductPriceToZero(@Param("price") Long id);


    /*
        -- "Lazy" Stream<T> for Spring Data JPA --
        - Jika kita menggunakan method "findAll....()" otomatis data akan di load ke dalam memory
        - Jika data sangat banyak, akan ada Exception OutOfMemory
        - Kita bisa menggunakan Stream sebagai solusi
        - Stream ini bersifat "Lazy" jadi semuanya tidak akan di load secara bersamaan
        - Melainkan di load saat digunakan, dan load nya juga sedikit demi sedikit
        - WAJIB RETURN "Stream<T>"
        - WAJIB MENGGUNAKAN PREFIX "streamAll....()"
     */
    Stream<Product> streamAllByCategory(Category category);

    Slice<Product> findAllByCategory(Category category, Pageable pageable); // Return "Slice<T>" untuk bisa mendapatkan informasi apakah ada "Next Page" atau "Previous Page"

    /*
        -- @Lock Annotation for "Pessimistic Locking" --
        - Mengunci row data ketika dibaca/diakses oleh satu transaksi
        - Transaksi lain yang mencoba mengakses data yang sama akan menunggu sampai lock dilepas / error kalau timeout

        1. PESSIMISTIC_READ             : Membaca data dengan read lock. Tidak membolehkan row diupdate oleh transaksi lain, tapi transaksi lain masih bisa baca.
        2. PESSIMISTIC_WRITE            : Membaca data dengan write lock. Tidak ada transaksi lain yang bisa baca atau update row yang sama.
        3. PESSIMISTIC_FORCE_INCREMENT  : Sama seperti PESSIMISTIC_WRITE, tapi juga memaksa versi entity (@Version) untuk di-increment.
     */
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Product> findByName(String name); // Untuk mencegah update data oleh transaksi lain, tetapi masih memperbolehkan read yang lain

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findById(Long id); // Paling ketat. Mencegah transaksi lain untuk baca atau tulis data yang sama.


    // Implementasi "Specification<T>"
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    // Implementasi "Projection"
    // Spring bisa otomatis mengetahui jika return valuenya adalah "Interface" dengan "Getter Method" ia akan mencari sesuai dengan jumlah "Getter Method" sesuai dengan PropertyName nya
    // Contohnya disini mencari dengan "Name" dan "Price"
    List<SimpleProduct> findAllByNameLike(String name); // Returnnya adalah List<Interface>, di dalam Interface ada 2 "Getter Method", dan nantinya querynya hanya "SELECT Name, Price ....." bukan "SELECT *"
    List<SimpleProductRecord> findAllById(Long id); // LEBIH BAIK MENGGUNAKAN "RECORD" karena tidak memerlukan Proxy (Reflection)
}
