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
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.alibaba.nacos")
@ServletComponentScan
@EnableScheduling
public class NacosApplication {

    public static void main(String[] args) {
        if (initEnv()) {
            // SpringApplicationUtil.run(NacosServerApplication.class, args);
            SpringApplication.run(NacosApplication.class, args);
        }
    }

    /**
     * 初始化运行环境
     */
    private static boolean initEnv() {
        // The System property name of Standalone mode 单机
        System.setProperty("nacos.standalone", "true");
        // 是否开启认证
        System.setProperty("nacos.core.auth.enabled", "false");
        // 日志目录
        // System.setProperty("server.tomcat.basedir", LogDirUtil.getLogDir() + "/nacos");
        return true;
    }
}
