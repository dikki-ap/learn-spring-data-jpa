package dikki_dev.learn_spring_data_jpa.repositories;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// "@Repository" Annotation sifatnya  optional, hanya untuk memberitahukan saja
// Karena Spring sudah otomatis tahu extend "JpaRepository<Model, PrimaryKeyValueType>"
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
