package ma.zrad.system.ref.core.infra.config;

import ma.zrad.system.batch.common.annotations.BusinessService;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        ZradRefCoreScanPackages.INFRA_REST,
        ZradRefCoreScanPackages.INFRA_REPOSITORY,
        ZradRefCoreScanPackages.INFRA_ADAPTER,
        ZradRefCoreScanPackages.INFRA_ENTITY,
        ZradRefCoreScanPackages.INFRA_MAPPER,
        ZradRefCoreScanPackages.BUSINESS_SERVICE,
        ZradRefCoreScanPackages.BUSINESS_PORT_OUT,
        ZradRefCoreScanPackages.DOMAIN_PORT_IN,
        ZradRefCoreScanPackages.DOMAIN_PORT_OUT
    }, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = BusinessService.class))
public class CoreAppConfig {

}
