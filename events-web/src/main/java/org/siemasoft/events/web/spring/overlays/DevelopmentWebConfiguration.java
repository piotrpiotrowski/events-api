package org.siemasoft.events.web.spring.overlays;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("development")
@PropertySource(value = {"classpath:spring-development.properties"})
public class DevelopmentWebConfiguration {

}
