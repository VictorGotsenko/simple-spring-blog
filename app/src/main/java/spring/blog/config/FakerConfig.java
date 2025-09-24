package spring.blog.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Это описание класса, который выполняет определенные действия.
 *
 * @author Имя автора
 * @version 1.0
 */
@Configuration
public class FakerConfig {

    /**
     * Это функция.
     *
     * @author Имя автора
     * @version 1.0
     * @return the name of the object
     */
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
