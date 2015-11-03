package org.siemasoft.events.core.spring.overlays;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("local")
@PropertySource(value = {"classpath:database/local.properties", "classpath:hibernate/local.properties"})
public class LocalConfiguration {
}
