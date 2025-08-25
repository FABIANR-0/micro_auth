package co.com.pragma.config;

import co.com.pragma.model.user.validation.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }

}
