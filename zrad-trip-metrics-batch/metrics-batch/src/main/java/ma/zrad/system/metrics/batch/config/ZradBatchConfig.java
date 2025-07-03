package ma.zrad.system.metrics.batch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ma.zrad.system.batch.common.annotations.BusinessService;
import ma.zrad.system.metrics.batch.infra.config.ZradScanPackages;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { ZradScanPackages.BATCH_API_FEIGN })
@ComponentScan(basePackages = {
        ZradScanPackages.INFRA_REPOSITORY,
        ZradScanPackages.INFRA_ADAPTER,
        ZradScanPackages.INFRA_MAPPER,
        ZradScanPackages.BUSINESS_SERVICE,
        ZradScanPackages.DOMAIN_PORT_IN,
        ZradScanPackages.DOMAIN_PORT_OUT
    }, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = BusinessService.class))
public class ZradBatchConfig {

    @Bean
    ObjectMapper objectMapper() {
        JsonMapper jsonMapper = JsonMapper.builder()
                .build();
        //jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return jsonMapper;
    }

}
