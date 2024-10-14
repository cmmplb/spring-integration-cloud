# 学习 SpringCloud 整合

spring 官网：https://spring.io/

Spring Cloud Alibaba-logo：https://sca.aliyun.com/

kubernetes：https://kubernetes.io/

spring-boot 和 spring-cloud 版本关系图

![版本关系图.png](doc%2Fimages%2F%E7%89%88%E6%9C%AC%E5%85%B3%E7%B3%BB%E5%9B%BE.png)

master 分支为最新版本 2023.0.3.x：

版本从新到旧排列，feature todo：

| 分支            | spring-boot    | spring-cloud | spring-cloud-alibaba | spring-cloud-kubernetes | 完成度 |
|---------------|----------------|--------------|----------------------|-------------------------|-----|
| 2023.0.3.x    | 3.3.4          | 2023.0.3     | 2023.0.1.2           | 3.1.3                   | ✅   |
| 2022.0.5.x    | 3.1.12         | 2022.0.5     | 2022.0.0.0           | 3.0.5                   | ❌   |
| 2021.0.9.x    | 2.7.18         | 2021.0.9     | 2021.0.6.1           | 2.1.9                   | ❌   |
| 2020.0.6.x    | 2.5.15         | 2020.0.6     | 2021.1               | 2.0.6                   | ✅   |
| Hoxton.SR12.x | 2.3.12.RELEASE | Hoxton.SR12  | 2.2.10-RC2           | 1.1.10.RELEASE          | ❌   |

**其他版本查看spring-parent.pom中配置的properties**