package cor.chrissy.community.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Configuration
@ComponentScan("cor.chrissy.community.service")
@MapperScan(basePackages = {
        "cor.chrissy.community.service.article.repository.mapper",
        "cor.chrissy.community.service.config.repository.mapper",
        "cor.chrissy.community.service.comment.repository.mapper",
        "cor.chrissy.community.service.notify.repository.mapper",
        "cor.chrissy.community.service.statistics.repository.mapper",
        "cor.chrissy.community.service.user.repository.mapper",
})
public class ServiceAutoConfig {
}
