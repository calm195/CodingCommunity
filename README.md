# coding 技术社区

原型，简单搭建了一个框架

### 开发环境

|      工具       | 版本        | 下载                                                                                                                     |
|:-------------:|:----------|------------------------------------------------------------------------------------------------------------------------|
|      jdk      | 1.8+      | [https://www.oracle.com/java/technologies/downloads/#java8](https://www.oracle.com/java/technologies/downloads/#java8) |
|     maven     | 3.4+      | [https://maven.apache.org/](https://maven.apache.org/)                                                                 |
|     mysql     | 5.7+/8.0+ | [https://www.mysql.com/downloads/](https://www.mysql.com/downloads/)                                                   |
|     redis     | 5.0+      | [https://redis.io/download/](https://redis.io/download/)                                                               |
| elasticsearch | 8.0.0+    | [https://www.elastic.co/cn/downloads/elasticsearch](https://www.elastic.co/cn/downloads/elasticsearch)                 |
|     nginx     | 1.10+     | [https://nginx.org/en/download.html](https://nginx.org/en/download.html)                                               |
|   rabbitmq    | 3.10.14+  | [https://www.rabbitmq.com/news.html](https://www.rabbitmq.com/news.html)                                               |
|    ali-oss    | 3.15.1    | [https://letsencrypt.org/](https://letsencrypt.org/)                                                                   |
|      git      | 2.34.1    | [http://github.com/](http://github.com/)                                                                               |
|    docker     | 4.10.0+   | [https://docs.docker.com/desktop/](https://docs.docker.com/desktop/)                                                   |
| let's encrypt | https证书   | [https://letsencrypt.org/](https://letsencrypt.org/)                                                                   |

## 使用的技术栈

后端技术栈

|         技术          | 说明                   | 官网                                                                                                   |
|:-------------------:|----------------------|------------------------------------------------------------------------------------------------------|
| Spring & SpringMVC  | Java全栈应用程序框架和WEB容器实现 | [https://spring.io/](https://spring.io/)                                                             |
|     SpringBoot      | Spring应用简化集成开发框架     | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)                     |
|    mybatis-plus     | 数据库orm框架             | [https://baomidou.com/](https://baomidou.com/)                                                       |
| mybatis PageHelper  | 数据库翻页插件              | [https://github.com/pagehelper/Mybatis-PageHelper](https://github.com/pagehelper/Mybatis-PageHelper) |
|    elasticsearch    | 近实时文本搜索              | [https://www.elastic.co/cn/elasticsearch/service](https://www.elastic.co/cn/elasticsearch/service)   |
|        redis        | 内存数据存储               | [https://redis.io](https://redis.io)                                                                 |
|      rabbitmq       | 消息队列                 | [https://www.rabbitmq.com](https://www.rabbitmq.com)                                                 |
|       mongodb       | NoSql数据库             | [https://www.mongodb.com/](https://www.mongodb.com/)                                                 |
|        nginx        | 服务器                  | [https://nginx.org](https://nginx.org)                                                               |
|       docker        | 应用容器引擎               | [https://www.docker.com](https://www.docker.com)                                                     |
|      hikariCP       | 数据库连接                | [https://github.com/brettwooldridge/HikariCP](https://github.com/brettwooldridge/HikariCP)           |
|         oss         | 对象存储                 | [https://letsencrypt.org/](https://letsencrypt.org/)                                                 |
|         jwt         | jwt登录                | [https://jwt.io](https://jwt.io)                                                                     |
|       lombok        | Java语言增强库            | [https://projectlombok.org](https://projectlombok.org)                                               |
|        guava        | google开源的java工具集     | [https://github.com/google/guava](https://github.com/google/guava)                                   |
|      thymeleaf      | html5模板引擎            | [https://www.thymeleaf.org](https://www.thymeleaf.org)                                               |
|       swagger       | API文档生成工具            | [https://swagger.io](https://swagger.io)                                                             |
| hibernate-validator | 验证框架                 | [hibernate.org/validator/](hibernate.org/validator/)                                                 |
|     quick-media     | 多媒体处理                | [https://github.com/liuyueyi/quick-media](https://github.com/liuyueyi/quick-media)                   |
|      liquibase      | 数据库版本管理              | [https://www.liquibase.com](https://www.liquibase.com)                                               |
|       jackson       | json/xml处理           | [https://www.jackson.com](https://www.jackson.com)                                                   |
|      ip2region      | ip地址                 | [https://github.com/zoujingli/ip2region](https://github.com/zoujingli/ip2region)                     |

## 配置文件说明

- resources
    - application.yml: 主配置文件入口
    - application-config.yml: 全局的站点信息配置文件
    - logback-spring.xml: 日志打印相关配置文件
    - schema-all.sql: 项目中所有表结构定义sql文件
    - init-data.sql: 初始化数据sql文件
    - schema.sql, test-data.sql: 开发阶段的sql文件，后续会删除，不用关注
    - liquibase: 由liquibase进行数据库表结构管理
- resources-env
    - xxx/application-dal.yml: 定义数据库相关的配置信息
    - xxx/application-image.yml: 定义上传图片的相关配置信息
    - xxx/application-web.yml: 定义web相关的配置信息

## 结构说明

```
CodingCommunity
├── common -- 定义一些通用的枚举、实体类，定义 DO\VO 等
├── core -- 核心工具/组件相关模块，如工具包 util， 通用的组件都放在这个模块（以包路径对模块功能进行拆分，如搜索、缓存、推荐等）
├── service -- 服务模块，业务相关的主要逻辑，DB 的操作都在这里
├── ui -- HTML 前端资源（包括 JavaScript、CSS、Thymeleaf 等）
├── web -- Web模块、HTTP入口、项目启动入口，包括权限身份校验、全局异常处理等
```

## 前端工程结构说明

- resources/static: 静态资源文件，如css/js/image，放在这里
- resources/templates: html相关页面
    - views: 业务相关的页面
        - 定义：
            - 页面/index.html:  这个index.html表示的是这个业务对应的主页面
            - 页面/模块/xxx.html:  若主页面又可以拆分为多个模块页面进行组合，则在这个页面下，新建一个模块目录，下面放对应的html文件
        - article-category-list: 对应 分类文章列表页面，
        - article-detail: 对应文章详情页
            - side-float-action-bar: 文章详情，左边的点赞/收藏/评论浮窗
            - side-recommend-bar: 文章详情右边侧边栏的sidebar
        - article-edit: 对应文章发布页
        - article-search-list: 对应文章搜索页
        - article-tag-list: 对应标签文章列表
        - column-detail：对应专栏阅读详情页
        - column-home: 对应专栏首页
        - home: 全站主页
        - login: 登录页面
        - notice: 通知页面
        - user: 用户个人页
    - error: 错误页面
    - components: 公用的前端页面组件

css 放在 static/css 中：

- components: 公共组件的css
    - navbar: 导航栏样式
    - footer: 底部样式
    - article-item: 文章块展示样式
    - article-footer: 文章底部（点赞、评论等）
    - side-column: 侧边栏（公告等）
- views: 主页面css(直接在主页面内部引入)
    - home: 主页样式
    - article-detail: 详情页样式
    - ...
- three: 第三方css
    - index: 第三方css集合
    - ...
- common: 公共组件的css集合 （直接在公共组件components/layout/header/index.html内引入）
- global: 全局样式（全局的样式控制，注意覆盖问题，直接在公共组件components/layout/header/index.html内引入）
