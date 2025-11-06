package dikki_dev.learn_spring_data_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Enable fungsi JPA Auditing untuk mengaktifkan fitur "timestamp" "created_date" dan "last_modified_date"
public class LearnSpringDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnSpringDataJpaApplication.class, args);
	}

}
