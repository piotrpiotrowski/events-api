package org.siemasoft.restfailed.resolver;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.siemasoft.restfailed.handler.DefaultRestExceptionHandler;
import org.siemasoft.restfailed.handler.RestExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.siemasoft.restfailed.support.HttpMessageConverterUtils.getDefaultHttpMessageConverters;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.web.servlet.HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE;

@Getter
@Setter
@Log4j2
public class RestHandlerExceptionResolver extends AbstractHandlerExceptionResolver implements InitializingBean {

    private final MethodParameter returnTypeMethodParam;

    private List<HttpMessageConverter<?>> messageConverters = getDefaultHttpMessageConverters();

    @Setter(AccessLevel.NONE)
    private Map<Class<? extends Exception>, RestExceptionHandler<? extends Exception>> exceptionHandlers = new LinkedHashMap<>();

    private MediaType defaultContentType = APPLICATION_XML;

    private ContentNegotiationManager contentNegotiationManager;

    private HandlerMethodReturnValueHandler responseProcessor;

    private HandlerMethodReturnValueHandler fallbackResponseProcessor;

    public static RestHandlerExceptionResolverBuilder builder() {
        return new RestHandlerExceptionResolverBuilder();
    }

    public RestHandlerExceptionResolver() {
        Method method = ClassUtils.getMethod(DefaultRestExceptionHandler.class, "handleException", Exception.class, HttpServletRequest.class);
        returnTypeMethodParam = new MethodParameter(method, -1);
        returnTypeMethodParam.getGenericParameterType();
    }

    @Override
    public void afterPropertiesSet() {
        FixedContentNegotiationStrategy fixedContentNegotiationStrategy = new FixedContentNegotiationStrategy(defaultContentType);
        if (contentNegotiationManager == null) {
            HeaderContentNegotiationStrategy headerContentNegotiationStrategy = new HeaderContentNegotiationStrategy();
            contentNegotiationManager = new ContentNegotiationManager(headerContentNegotiationStrategy, fixedContentNegotiationStrategy);
        }
        responseProcessor = new HttpEntityMethodProcessor(messageConverters, contentNegotiationManager);
        fallbackResponseProcessor = new HttpEntityMethodProcessor(messageConverters, new ContentNegotiationManager(fixedContentNegotiationStrategy));
    }

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        ResponseEntity<?> entity = handleException(exception, request);
        try {
            processResponse(entity, new ServletWebRequest(request, response));
        } catch (Exception ex) {
            log.error("Failed to process error response: {}", entity, ex);
            return null;
        }
        return new ModelAndView();
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<?> handleException(Exception exception, HttpServletRequest request) {
        request.removeAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
        RestExceptionHandler handler = resolveExceptionHandler(exception.getClass());
        if (handler == null) {
            log.warn("No exception handler found to handle exception: {}", exception.getClass().getName());
            return null;
        }
        log.debug("Handling exception {} with response factory: {}", exception.getClass().getName(), handler);
        return handler.handleException(exception, request);
    }

    private RestExceptionHandler<? extends Exception> resolveExceptionHandler(Class<? extends Exception> exceptionClass) {
        for (Class clazz = exceptionClass; clazz != Throwable.class; clazz = clazz.getSuperclass()) {
            if (exceptionHandlers.containsKey(clazz)) {
                return exceptionHandlers.get(clazz);
            }
        }
        return null;
    }

    private void processResponse(ResponseEntity<?> entity, NativeWebRequest webRequest) throws Exception {
        MethodParameter methodParameter = new MethodParameter(returnTypeMethodParam);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        try {
            responseProcessor.handleReturnValue(entity, methodParameter, mavContainer, webRequest);
        } catch (HttpMediaTypeNotAcceptableException ex) {
            log.debug("Requested media type is not supported, falling back to default one");
            fallbackResponseProcessor.handleReturnValue(entity, methodParameter, mavContainer, webRequest);
        }
    }

    public void putAllExceptionHandlersIfAbsent(Map<Class<? extends Exception>, RestExceptionHandler<? extends Exception>> exceptionHandlers) {
        for (Map.Entry<Class<? extends Exception>, RestExceptionHandler<? extends Exception>> entry : exceptionHandlers.entrySet()) {
            if (!this.exceptionHandlers.containsKey(entry.getKey())) {
                this.exceptionHandlers.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
