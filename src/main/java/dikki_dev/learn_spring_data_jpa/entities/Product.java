package dikki_dev.learn_spring_data_jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")

// Membuat "NamedQuery" untuk Product, mencari berdasarkan "name"
@NamedQueries({
        @NamedQuery(
                name = "Product.searchProductUsingName", // Awalannya harus sesuai nama Entity
                query = "SELECT p FROM Product p WHERE p.name = :name"
        )
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
