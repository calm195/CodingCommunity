# coding 技术社区

原型，简单搭建了一个框架

## 环境要求

- Java8 up
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

## 结构说明

- core: 项目通用的组件与模块
- service: 项目业务核心逻辑
- web:  项目入口与权限校验，全局处理
- ui:  项目ui资源
- common: 项目通用数据结构等
