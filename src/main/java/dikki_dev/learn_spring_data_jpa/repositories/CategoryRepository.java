package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// "@Repository" Annotation sifatnya  optional, hanya untuk memberitahukan saja
// Karena Spring sudah otomatis tahu extend "JpaRepository<Model, PrimaryKeyValueType>"
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /*
        -- Query Method --
        - Spring Data JPA memiliki powerfull feature untuk mengambil data dari DB hanya membuat function saja
        - Nama function akan diterjemahkan secara otomatis menjadi query, dengan format tertentu

        1. Prefix "findFirst...."      --> Mengembalikan 1 data, bertipe Optional<T>
        2. Prefix "findAll......"      --> Mengembalikan List data, bertipe List<T>

        Akan dilanjutkan dengan kata "By" dan "PropertyName"
        1. findFirstByName              --> SELECT * FROM TABLE_NAME WHERE Name = '...' (Mengembalikan 1 Data paling awal)
        2. findAllByCategory            --> SELECT * FROM TABLE_NAME WHERE Category = '...' (Mengembalikan List)

        Docs: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     */

    Optional<Category> findFirstByNameLike(String name); // name LIKE 'name'
    Optional<Category> findFirstByNameEquals(String name); // Name = 'name'
    List<Category> findAllByNameEquals(String name); // Name = 'name'
    List<Category> findAllByNameLike(String name); // name LIKE 'name'
}
