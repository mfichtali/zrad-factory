package ma.zrad.system.metrics.batch.api.feign.common;

import feign.Logger;
import feign.Retryer;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

public class FeignSupportConfig {

    @Value("${application.feign.client.config.retry.enabled:true}")
    private boolean retryEnabled;

    @Value("${application.feign.client.config.retry.max-attempts:3}")
    private int retryMaxAttempt;

    @Value("${application.feign.client.config.retry.interval:1000}")
    private long retryInterval;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Encoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new FeignClientRetryer(retryMaxAttempt, retryInterval);
    }

}
