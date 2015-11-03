package org.siemasoft.events.core.spring.overlays;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("development")
@PropertySource(value = {"classpath:database/development.properties", "classpath:hibernate/development.properties"})
public class DevelopmentConfiguration {
}
