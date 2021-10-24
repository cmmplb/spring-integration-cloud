/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author nacos
 * <p>
 * nacos console 源码运行，方便开发 生产建议从官网下载最新版配置运行
 */

@SpringBootApplication
public class SpringCloudNacosServerApplication {

    public static void main(String[] args) {
        if (initEnv()) {
            SpringApplication.run(SpringCloudNacosServerApplication.class, args);
        }
    }

    /**
     * 初始化运行环境
     */
    private static boolean initEnv() {
        System.setProperty("nacos.standalone", "true"); // The System property name of Standalone mode 单机
        System.setProperty("nacos.core.auth.enabled", "false"); // 是否开启认证
        System.setProperty("server.tomcat.basedir", "logs"); // 日志目录
        return true;
    }
}
