package org.siemasoft.events.core.spring.overlays;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("production")
@PropertySource(value = {"classpath:hibernate/production.properties"})
public class ProductionCoreConfiguration {

}
