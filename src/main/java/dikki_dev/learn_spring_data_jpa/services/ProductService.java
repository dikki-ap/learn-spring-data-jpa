package dikki_dev.learn_spring_data_jpa.services;

import dikki_dev.learn_spring_data_jpa.entities.Product;
import dikki_dev.learn_spring_data_jpa.repositories.ProductRepository;
import dikki_dev.learn_spring_data_jpa.specs.ProductSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> search(
            String name,
            String category,
            Long minPrice,
            Long maxPrice,
            Pageable pageable
    ) {
        // "allOf()" itu seperti "where()"
        Specification<Product> spec = Specification.allOf(
                ProductSpecs.nameContains(name),
                ProductSpecs.categoryNameEquals(category),
                ProductSpecs.priceGte(minPrice),
                ProductSpecs.priceLte(maxPrice)
        );

        return productRepository.findAll(spec, pageable);
    }
}
