package dikki_dev.learn_spring_data_jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    // Menambahkan "Auditrail" timestamp
    @CreatedDate // Wajib enable "@EnableJpaAuditing"
    @Column(name = "created_date")
    private Instant createdDate;

    // Menambahkan "Auditrail" timestamp
    @LastModifiedDate // Wajib enable "@EnableJpaAuditing"
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
}
