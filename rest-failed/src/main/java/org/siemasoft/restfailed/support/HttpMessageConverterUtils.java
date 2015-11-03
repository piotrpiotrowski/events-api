package org.siemasoft.restfailed.support;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public abstract class HttpMessageConverterUtils {

    private static final ClassLoader CLASSLOADER = HttpMessageConverterUtils.class.getClassLoader();

    public static boolean isJaxb2Present() {
        return ClassUtils.isPresent("javax.xml.bind.Binder", CLASSLOADER);
    }

    public static boolean isJackson2Present() {
        return ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", CLASSLOADER) &&
                ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", CLASSLOADER);
    }

    public static List<HttpMessageConverter<?>> getDefaultHttpMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        // See SPR-7316
        stringConverter.setWriteAcceptCharset(false);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(stringConverter);
        converters.add(new ResourceHttpMessageConverter());
        if (isJaxb2Present()) {
            converters.add(new Jaxb2RootElementHttpMessageConverter());
        }
        if (isJackson2Present()) {
            converters.add(new MappingJackson2HttpMessageConverter());
        }
        return converters;
    }
}
