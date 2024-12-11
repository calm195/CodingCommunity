package cor.chrissy.community.web;

import cor.chrissy.community.web.hook.interceptor.GlobalViewInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@ServletComponentScan
@SpringBootApplication
public class CodingCommunityApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(CodingCommunityApplication.class, args);
    }

    @Resource
    private GlobalViewInterceptor globalViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalViewInterceptor);
    }
}
