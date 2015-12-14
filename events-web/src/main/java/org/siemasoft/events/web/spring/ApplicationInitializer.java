package org.siemasoft.events.web.spring;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;

@Log4j2
public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public static final String DEFAULT_SPRING_ACTIVE_PROFILE = "development";

    public static final String SPRING_ACTIVE_PROFILE = "spring.profiles.active";

    public static final String ENVIRONMENT_VARIABLE_SPRING_ACTIVE_PROFILE = "spring_profiles_active";

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
        String springActiveProfile = resolveSpringActiveProfile();
        registration.setInitParameter(THROW_EXCEPTION_IF_NO_HANDLER_FOUND, "true");
        registration.setInitParameter(SPRING_ACTIVE_PROFILE, springActiveProfile);
        log.info("Active spring profile is {}", springActiveProfile);
    }

    private String resolveSpringActiveProfile() {
        String springActiveProfile = System.getenv(ENVIRONMENT_VARIABLE_SPRING_ACTIVE_PROFILE);
        return springActiveProfile == null ? DEFAULT_SPRING_ACTIVE_PROFILE : springActiveProfile;
    }
}
