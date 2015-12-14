package org.siemasoft.events.web.spring;

import org.siemasoft.events.core.spring.CoreConfiguration;
import org.siemasoft.events.core.spring.PropertyResolver;
import org.siemasoft.restfailed.resolver.RestHandlerExceptionResolver;
import org.siemasoft.restfailed.support.HttpMessageConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableSpringDataWebSupport
@Import(CoreConfiguration.class)
@ComponentScan(basePackages = {"org.siemasoft.events.web"})
@PropertySource(value = {"classpath:spring.properties"})
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private PropertyResolver propertyResolver;

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        super.configureHandlerExceptionResolvers(resolvers);
        resolvers.add(exceptionHandlerExceptionResolver());
        resolvers.add(restExceptionResolver());
    }

    @Bean
    public RestHandlerExceptionResolver restExceptionResolver() {
        return RestHandlerExceptionResolver.builder()
                .defaultContentType(MediaType.APPLICATION_JSON)
                .withDefaultHandlers(true)
                .build();
    }

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(HttpMessageConverterUtils.getDefaultHttpMessageConverters());
        return resolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(propertyResolver.getRequiredProperty("allowed_origins"));
    }
}
