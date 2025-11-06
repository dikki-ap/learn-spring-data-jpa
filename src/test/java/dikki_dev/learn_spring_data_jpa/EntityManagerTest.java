package dikki_dev.learn_spring_data_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntityManagerTest {

    // Sudah otomatis dibuat jadi Bean oleh Spring, jadi bisa gunakan DI
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testEntityManagerFactory(){
        Assertions.assertNotNull(entityManagerFactory);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Assertions.assertNotNull(entityManager);

        entityManager.close();
    }
}
