package dikki_dev.learn_spring_data_jpa.specs;

import dikki_dev.learn_spring_data_jpa.entities.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class that contains reusable {@link Specification} definitions for querying {@link Product} entities.
 * <p>
 * This class provides static helper methods to create dynamic filtering criteria using Spring Data JPA Specifications.
 * Each method returns a {@link Specification} that can be composed with others using
 * {@code Specification.where()}, {@code and()}, or {@code or()}.
 * </p>
 * <pre>
 * Example Usage:
 * Specification<Product> spec = Specification
 *      .where(ProductSpecs.nameContains("phone"))
 *      .and(ProductSpecs.priceGte(50000L))
 *      .and(ProductSpecs.categoryNameEquals("Electronics"));
 *
 * List<Product> filteredProducts = productRepository.findAll(spec);
 * </pre>
 */
public final class ProductSpecs {

    private ProductSpecs() {
        // Prevent instantiation
    }

    /**
     * Filters products whose name contains the given keyword (case-insensitive).
     * <p>
     * If the keyword is {@code null} or blank, this specification returns a neutral predicate
     * (i.e. {@code cb.conjunction()}), which has no effect on the final result when combined.
     *
     * @param keyword the keyword to search in product names (case-insensitive)
     * @return a {@link Specification} that applies a LIKE filter on the product name
     */
    public static Specification<Product> nameContains(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }

    /**
     * Filters products whose price is greater than or equal to the given value.
     * <p>
     * If the minimum price is {@code null}, this specification returns a neutral predicate.
     *
     * @param min the minimum price threshold
     * @return a {@link Specification} that applies a {@code >=} filter on the product price
     */
    public static Specification<Product> priceGte(Long min) {
        return (root, query, cb) ->
                min == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    /**
     * Filters products whose price is less than or equal to the given value.
     * <p>
     * If the maximum price is {@code null}, this specification returns a neutral predicate.
     *
     * @param max the maximum price threshold
     * @return a {@link Specification} that applies a {@code <=} filter on the product price
     */
    public static Specification<Product> priceLte(Long max) {
        return (root, query, cb) ->
                max == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("price"), max);
    }

    /**
     * Filters products by exact category name match.
     * <p>
     * If the category name is {@code null} or blank, this specification returns a neutral predicate.
     *
     * @param name the exact category name to match
     * @return a {@link Specification} that applies an equality filter based on the category name
     */
    public static Specification<Product> categoryNameEquals(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? cb.conjunction()
                        : cb.equal(root.join("category").get("name"), name);
    }
}
