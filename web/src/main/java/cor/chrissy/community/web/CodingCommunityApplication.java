package cor.chrissy.community.web;

import cor.chrissy.community.core.util.SocketUtil;
import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.web.config.GlobalViewConfig;
import cor.chrissy.community.web.global.CodingCommunityExceptionHandler;
import cor.chrissy.community.web.hook.interceptor.GlobalViewInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Slf4j
@ServletComponentScan
@SpringBootApplication
public class CodingCommunityApplication implements WebMvcConfigurer, ApplicationRunner {

    private Integer webPort = null;

    public static void main(String[] args) {
        SpringApplication.run(CodingCommunityApplication.class, args);
    }

    @Resource
    private GlobalViewInterceptor globalViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalViewInterceptor);
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, new CodingCommunityExceptionHandler());
    }

    @Bean
    @ConditionalOnExpression(value = "#{'dev'.equals(environment.getProperty('env.name'))}")
    public TomcatConnectorCustomizer customServerPortTomcatConnectorCustomizer() {
        int port = SocketUtil.findAvailableTcpPort(8000, 10000, 8080);
        if (port != 8080) {
            log.info("TCP Server port is {}", port);
            webPort = port;
        }
        return connector -> connector.setPort(port);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        GlobalViewConfig config = SpringUtil.getBean(GlobalViewConfig.class);;
        if (webPort != null) {
            config.setHost("http://localhost:" + webPort);
        }
        log.info("application starts successfully, please click: {}", config.getHost());
    }
}
