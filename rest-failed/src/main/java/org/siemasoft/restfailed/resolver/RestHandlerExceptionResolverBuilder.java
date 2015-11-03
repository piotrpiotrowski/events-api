package org.siemasoft.restfailed.resolver;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.siemasoft.restfailed.exception.RestFailedException;
import org.siemasoft.restfailed.handler.*;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.NONE;
import static org.springframework.util.StringUtils.hasText;

@Setter
@Accessors(fluent = true)
public class RestHandlerExceptionResolverBuilder {

    private final Map<Class<? extends Exception>, RestExceptionHandler<? extends Exception>> exceptionHandlers = new HashMap<>();

    @Setter(NONE)
    private MediaType defaultContentType;

    private ContentNegotiationManager contentNegotiationManager;

    private List<HttpMessageConverter<?>> httpMessageConverters;

    private boolean withDefaultHandlers = true;

    public RestHandlerExceptionResolver build() {
        RestHandlerExceptionResolver resolver = new RestHandlerExceptionResolver();
        if (withDefaultHandlers) {
            resolver.putAllExceptionHandlersIfAbsent(getDefaultHandlers());
        }
        resolver.putAllExceptionHandlersIfAbsent(exceptionHandlers);
        if (httpMessageConverters != null) {
            resolver.setMessageConverters(httpMessageConverters);
        }
        ContentNegotiationManager contentNegotiationManager = this.contentNegotiationManager != null ? this.contentNegotiationManager : new ContentNegotiationManager();
        resolver.setContentNegotiationManager(contentNegotiationManager);
        if (defaultContentType != null) {
            resolver.setDefaultContentType(defaultContentType);
        }
        resolver.afterPropertiesSet();
        return resolver;
    }

    public RestHandlerExceptionResolverBuilder defaultContentType(MediaType mediaType) {
        this.defaultContentType = mediaType;
        return this;
    }

    public RestHandlerExceptionResolverBuilder defaultContentType(String mediaType) {
        defaultContentType(hasText(mediaType) ? MediaType.parseMediaType(mediaType) : null);
        return this;
    }

    public <E extends Exception> RestHandlerExceptionResolverBuilder addHandler(Class<? extends E> exceptionClass, RestExceptionHandler<E> exceptionHandler) {
        exceptionHandlers.put(exceptionClass, exceptionHandler);
        return this;
    }

    private Map<Class<? extends Exception>, RestExceptionHandler<? extends Exception>> getDefaultHandlers() {
        Map<Class<? extends Exception>, RestExceptionHandler<? extends Exception>> map = new HashMap<>();
        map.put(HttpRequestMethodNotSupportedException.class, new HttpRequestMethodNotSupportedExceptionHandler());
        map.put(HttpMediaTypeNotSupportedException.class, new HttpMediaTypeNotSupportedExceptionHandler());
        map.put(MethodArgumentNotValidException.class, new MethodArgumentNotValidExceptionHandler());
        map.put(NoSuchRequestHandlingMethodException.class, DefaultRestExceptionHandler.createForNotFound());
        map.put(HttpMediaTypeNotAcceptableException.class, DefaultRestExceptionHandler.createForNotAcceptable());
        map.put(MissingServletRequestParameterException.class, DefaultRestExceptionHandler.createForBadRequest());
        map.put(ServletRequestBindingException.class, DefaultRestExceptionHandler.createForBadRequest());
        map.put(ConversionNotSupportedException.class, DefaultRestExceptionHandler.createForInternalServerError());
        map.put(TypeMismatchException.class, DefaultRestExceptionHandler.createForBadRequest());
        map.put(HttpMessageNotReadableException.class, DefaultRestExceptionHandler.createForUnprocessableEntity());
        map.put(HttpMessageNotWritableException.class, DefaultRestExceptionHandler.createForInternalServerError());
        map.put(MissingServletRequestPartException.class, DefaultRestExceptionHandler.createForBadRequest());
        map.put(Exception.class, DefaultRestExceptionHandler.createForInternalServerError());
        map.put(NoHandlerFoundException.class, DefaultRestExceptionHandler.<NoSuchRequestHandlingMethodException>createForNotFound());
        map.put(RestFailedException.class, new RestFailedExceptionHandler());
        return map;
    }
}
