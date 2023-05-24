package com.salam.libs.feign.elm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.*;
import java.util.stream.Stream;

@ConfigurationProperties(prefix = "salam.web.client")
public class SalamWebClientConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware,
        ResourceLoaderAware {

    private Map<String, ClientConfiguration> config = new HashMap<>();
    private Environment env;
    private ResourceLoader resourceLoader;

    private final static String DEFAULT_CLIENT = "default";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.config = Binder.get(env)
                .bind("salam.web.client", Bindable.mapOf(String.class, ClientConfiguration.class))
                .orElseThrow(IllegalStateException::new);

        var annotatedTypeScanner = new AnnotatedTypeScanner(FeignClient.class);
        var candidateClients = annotatedTypeScanner.findTypes("com.salam.libs.feign.elm.client");

        var configMap = new HashMap<Class<?>, ClientConfiguration>();
        candidateClients.forEach(candidateClient -> {
            FeignClient annotation = candidateClient.getAnnotation(FeignClient.class);
            if (config.containsKey(annotation.name())) {
                configMap.put(candidateClient, config.get(annotation.name()));
            }
        });

        var defaultConfig = config.get(DEFAULT_CLIENT);
        configMap.forEach((aClass, clientConfiguration) -> {
            var annotation = aClass.getAnnotation(FeignClient.class);
            var clientName = annotation.name();

            var beanDefinition = registerClient(aClass, clientConfiguration, defaultConfig);
            var beanName = String.format("%sClient", clientName);

            registry.registerBeanDefinition(beanName, beanDefinition);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private BeanDefinition registerClient(Class<?> clientClass,
                                          ClientConfiguration config,
                                          ClientConfiguration defaultConfig) {
        var client = buildFeignClient(clientClass, config, defaultConfig);
        var beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(client.getClass());
        beanDefinition.setInstanceSupplier(() -> client);

        return beanDefinition;
    }

    @Bean
    public ObjectMapper customObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Decoder feignDecoder() {
        var jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());

        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;

        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    @Bean
    Encoder feignEncoder() {
        var jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());

        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;

        return new SpringEncoder(objectFactory);
    }

    @Bean
    Contract contract() {
        var contract = new SpringMvcContract();
        contract.setResourceLoader(resourceLoader);

        return contract;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private Object buildFeignClient(Class<?> clientClass, ClientConfiguration config,
                                    ClientConfiguration defaultConfig) {
        var clientBuilder = Feign.builder()
                .contract(contract())
                .logger(new Slf4jLogger())
                .logLevel(firstNonNull(config.getLoggerLevel(),
                        Optional.ofNullable(defaultConfig)
                                .map(ClientConfiguration::getLoggerLevel).orElse(null),
                        Logger.Level.NONE))
                .retryer(new AppRetryer())
                .errorDecoder(new AppErrorDecoder(
                        Optional.ofNullable(defaultConfig)
                                .map(ClientConfiguration::getRetry).orElse(null),
                        config.getRetryMethod()
                ))
                .encoder(feignEncoder())
                .decoder(feignDecoder());

        if (defaultConfig != null) {
            addDefaultRequestHeaders(clientBuilder, defaultConfig.getDefaultRequestHeaders());
            addConnectionConfig(clientBuilder, defaultConfig);
        }

        addDefaultRequestHeaders(clientBuilder, config.getDefaultRequestHeaders());
        addConnectionConfig(clientBuilder, config);

        return clientBuilder.target(clientClass, config.getUrl());
    }

    private void addConnectionConfig(Feign.Builder clientBuilder, ClientConfiguration config) {
        if (config.getConnectTimeout() != null && config.getReadTimeout() != null) {
            clientBuilder.options(new Request.Options(config.getConnectTimeout(), config.getReadTimeout()));
        } else {
            clientBuilder.options(new Request.Options());
        }
    }

    private void addDefaultRequestHeaders(Feign.Builder clientBuilder, Map<String, Collection<String>> headers) {
        if (headers != null) {
            clientBuilder.requestInterceptor(requestTemplate -> {
                headers.forEach(requestTemplate::header);
            });
        }
    }

    private static <T> T firstNonNull(final T... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    @Data
    public static class ClientConfiguration {
        private String url;
        private Logger.Level loggerLevel;
        private Integer connectTimeout;
        private Integer readTimeout;
        private RetryConfig retry;
        private Map<String, RetryConfig> retryMethod;
        private List<Class<RequestInterceptor>> requestInterceptors;
        private Class<ResponseInterceptor> responseInterceptor;
        private Map<String, Collection<String>> defaultRequestHeaders;
    }

    @Data
    public static class RetryConfig {
        private Integer maxAttempts;
        private Long period;
        private Long maxPeriod;
        private List<Integer> retryCodes = List.of(500, 502, 503, 504);

        public Retryer create() {
            return new Retryer.Default(period, maxPeriod, maxAttempts);
        }
    }

    static class AppRetryException extends RetryableException {
        private final RetryConfig retryConfig;

        public AppRetryException(RetryConfig retryConfig, int status,
                                 Request.HttpMethod httpMethod, Request request) {
            super(status, "", httpMethod, null, request);
            this.retryConfig = retryConfig;
        }

        public RetryConfig getRetryConfig() {
            return retryConfig;
        }
    }

    static class AppErrorDecoder implements ErrorDecoder {
        private final RetryConfig clientRetryConfig;
        private final Map<String, RetryConfig> methodRetryConfigs;
        private final ErrorDecoder errorDecoder = new ErrorDecoder.Default();

        public AppErrorDecoder(RetryConfig clientRetryConfig, Map<String, RetryConfig> methodRetryConfigs) {
            this.clientRetryConfig = clientRetryConfig;
            this.methodRetryConfigs = Optional.ofNullable(methodRetryConfigs).orElseGet(Map::of);
        }

        @Override
        public Exception decode(String methodKey, Response response) {
            RetryConfig retryConfig = null;
            var methodName = response.request().requestTemplate().methodMetadata().method().getName();
            if (methodRetryConfigs.containsKey(methodName)) {
                retryConfig = methodRetryConfigs.get(methodName);
            } else if (clientRetryConfig != null) {
                retryConfig = clientRetryConfig;
            }

            var status = response.status();
            if (retryConfig != null && retryConfig.retryCodes.contains(status)) {
                var method = response.request().httpMethod();
                return new AppRetryException(retryConfig, status, method, response.request());
            }

            return errorDecoder.decode(methodKey, response);
        }
    }

    static class AppRetryer implements Retryer {
        private Retryer retryer;

        private Retryer initAndGet(RetryConfig retryConfig) {
            if (retryer == null) {
                retryer = retryConfig.create();
            }

            return retryer;
        }

        @Override
        public void continueOrPropagate(RetryableException e) {
            if (e instanceof AppRetryException) {
                RetryConfig retryConfig = ((AppRetryException) e).getRetryConfig();
                initAndGet(retryConfig).continueOrPropagate(e);
            } else {
                throw e;
            }
        }

        @Override
        public Retryer clone() {
            return new AppRetryer();
        }
    }
}
