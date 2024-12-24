# coding 技术社区

原型，简单搭建了一个框架

## 环境要求

- Java8
- Maven

## 部署

1. `git clone` 本项目
2. 数据库导入web包下的resource [test-data.sql](/web/src/main/resources/test-data.sql)
3. 直接运行

## 使用的技术栈

- springboot 2.7
- mysql 8
- mybatis 3.5
- lombok
- thymeleaf 前端渲染

## 配置文件说明

- resources
    - application.yml: 主配置文件入口
    - application-config.yml: 全局的站点信息配置文件
    - logback-spring.xml: 日志打印相关配置文件
    - schema-all.sql: 项目中所有表结构定义sql文件
    - init-data.sql: 初始化数据sql文件
    - schema.sql, test-data.sql: 开发阶段的sql文件，后续会删除，不用关注
- resources-env
    - xxx/application-dal.yml: 定义数据库相关的配置信息
    - xxx/application-image.yml: 定义上传图片的相关配置信息
    - xxx/application-web.yml: 定义web相关的配置信息

## 结构说明

- core: 项目通用的组件与模块
- service: 项目业务核心逻辑
- web:  项目入口与权限校验，全局处理
- ui:  项目ui资源
- common: 项目通用数据结构等

## 前端工程结构说明

### 前端页面都放在 ui 模块中

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


### 前端 css 全部放在 static/css 中

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

