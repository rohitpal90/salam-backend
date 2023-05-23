package com.salam.libs.feign.elm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
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

            var beanDefinition = registerClient(clientName, aClass, clientConfiguration, defaultConfig);
            var beanName = String.format("%sClient", clientName);

            registry.registerBeanDefinition(beanName, beanDefinition);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private BeanDefinition registerClient(String clientName, Class<?> clientClass,
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
                .logLevel(firstNonNull(config.getLoggerLevel(),
                        Optional.ofNullable(defaultConfig)
                                .map(ClientConfiguration::getLoggerLevel).orElse(null),
                        Logger.Level.NONE))
                .encoder(feignEncoder())
                .decoder(feignDecoder());

        if (defaultConfig != null) {
            addLoggers(clientBuilder, defaultConfig.getLoggerLevel());
            addRequestHeaders(clientBuilder, defaultConfig.getDefaultRequestHeaders());
            addConnectionConfig(clientBuilder, defaultConfig);
        }

        addConnectionConfig(clientBuilder, config);
        addLoggers(clientBuilder, firstNonNull(config.getLoggerLevel(), Logger.Level.NONE));
        addRequestHeaders(clientBuilder, config.getDefaultRequestHeaders());

        return clientBuilder.target(clientClass, config.getUrl());
    }

    private void addConnectionConfig(Feign.Builder clientBuilder, ClientConfiguration config) {
        if (config.getConnectTimeout() != null && config.getReadTimeout() != null) {
            clientBuilder.options(new Request.Options(config.getConnectTimeout(), config.getReadTimeout()));
        } else {
            clientBuilder.options(new Request.Options());
        }
    }

    private void addLoggers(Feign.Builder clientBuilder, Logger.Level level) {
        clientBuilder.logLevel(level);
    }

    private void addRequestHeaders(Feign.Builder clientBuilder, Map<String, Collection<String>> headers) {
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
        private Class<ErrorDecoder> errorDecoder;
        private List<Class<RequestInterceptor>> requestInterceptors;
        private Class<ResponseInterceptor> responseInterceptor;
        private Map<String, Collection<String>> defaultRequestHeaders;
        private Map<String, Collection<String>> defaultQueryParameters;
    }

    @Data
    public static class RetryConfig {
        private Integer maxAttempts;
        private Long period;
        private Long maxPeriod;
    }
}
