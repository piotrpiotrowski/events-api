package org.siemasoft.events.core.spring.overlays;

import org.siemasoft.events.core.spring.PropertyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@Profile("development")
@PropertySource(value = {"classpath:database-development.properties", "classpath:hibernate/development.properties"})
public class DevelopmentCoreConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public PropertyResolver propertyResolver() {
        return new PropertyResolver() {
            @Override
            public String getRequiredProperty(String key) {
                return environment.getRequiredProperty(key);
            }
        };
    }
}
