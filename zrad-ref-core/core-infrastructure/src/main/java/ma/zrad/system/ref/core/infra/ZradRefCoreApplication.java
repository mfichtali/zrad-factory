package ma.zrad.system.ref.core.infra;

import ma.zrad.system.batch.common.utils.ServiceUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ZradRefCoreApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ZradRefCoreApplication.class, args);
        Environment env = context.getEnvironment();
        ServiceUtils.printRunCompleted(env);
    }
}
