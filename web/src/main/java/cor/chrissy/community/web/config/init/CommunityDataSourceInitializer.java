package cor.chrissy.community.web.config.init;

import cor.chrissy.community.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.*;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Slf4j
@Configuration
public class CommunityDataSourceInitializer {

//    @Value("classpath:schema-all.sql")
//    private Resource schemaSql;
//
//    @Value("classpath:init-data.sql")
//    private Resource initData;

    @Value("${spring.liquibase.enabled:true}")
    private Boolean liquibaseEnabled;

    @Value("${spring.liquibase.change-log}")
    private String liquibaseChangeLog;

    @Value("${database.name}")
    private String database;

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        // 设置数据源
        initializer.setDataSource(dataSource);
        boolean enabled = needInit(dataSource);
        initializer.setEnabled(enabled);
        initializer.setDatabasePopulator(databasePopulator(enabled));
        return initializer;
    }

    private DatabasePopulator databasePopulator(boolean enabled) {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        if (enabled && !liquibaseEnabled) {
            populator.addScripts(DbChangeSetLoader.loadDbChangeSetResources(liquibaseChangeLog).toArray(new ClassPathResource[]{}));
            populator.setSeparator(";");
            log.info("no liquibase database, init database by scripts");
        }
        return populator;
    }

    /**
     * 检测一下数据库中表是否存在，若存在则不初始化；否则基于 schema-all.sql 进行初始化表
     *
     * @param dataSource
     * @return
     */
    private boolean needInit(DataSource dataSource) {
        if (autoInitDatabase()) {
            return true;
        }
        // 根据是否存在表来判断是否需要执行sql操作
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List list = jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.TABLES where table_name = 'user_info' and table_schema = '" + database + "';");
        return CollectionUtils.isEmpty(list);
    }


    /**
     * 数据库不存在时，尝试创建数据库
     */
    private boolean autoInitDatabase() {
        // 查询失败，可能是数据库不存在，尝试创建数据库之后再次测试
        URI url = URI.create(SpringUtil.getConfig("spring.datasource.url").substring(5));
        String uname = SpringUtil.getConfig("spring.datasource.username");
        String pwd = SpringUtil.getConfig("spring.datasource.password");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + url.getHost() + ":" + url.getPort() +
                "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true", uname, pwd);
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery("select schema_name from information_schema.schemata where schema_name = '" + database + "'");
            if (!set.next()) {
                // 不存在时，创建数据库
                String createDb = "CREATE DATABASE IF NOT EXISTS " + database;
                connection.setAutoCommit(false);
                statement.execute(createDb);
                connection.commit();
                log.info("创建数据库（{}）成功", database);
                if (set.isClosed()) {
                    set.close();
                }
                return true;
            }
            set.close();
            log.info("数据库已存在，无需初始化");
            return false;
        } catch (SQLException e2) {
            throw new RuntimeException(e2);
        }
    }
}
