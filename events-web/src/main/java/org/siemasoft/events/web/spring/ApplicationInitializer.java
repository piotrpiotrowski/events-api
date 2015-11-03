package org.siemasoft.events.web.spring;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.Properties;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public static final String SPRING_ACTIVE_PROFILE = "spring.profiles.active";

    public static final String THROW_EXCEPTION_IF_NO_HANDLER_FOUND = "throwExceptionIfNoHandlerFound";

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        Properties properties = getProperties();
        registration.setInitParameter(THROW_EXCEPTION_IF_NO_HANDLER_FOUND, "true");
        registration.setInitParameter(SPRING_ACTIVE_PROFILE, properties.getProperty(SPRING_ACTIVE_PROFILE));
    }

    private Properties getProperties() {
        try {
            return PropertiesLoaderUtils.loadAllProperties("application.properties");
        } catch (IOException e) {
            throw new IllegalArgumentException("Properties file 'application.properties' has to be on classpath");
        }
    }
}
