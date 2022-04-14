解压apollo-quick-start-1.9.2.zip

导入sql

修改demo.sh脚本里的数据库连接信息

./demo.sh start启动

http://localhost:8070/
用户名apollo，密码admin




appollo-master 相当于是源码，apollo-build-scripts-master相当于将好几个服务集成到一块，用shell 脚本demo.sh 一键启动

将apollo-build-scripts-master 文件夹发送到虚拟机

配置demo.sh
------------------------------------------------------------------------------
#!/bin/bash

# apollo config db info
apollo_config_db_url=jdbc:mysql://虚拟机ip或者localhost:3306/ApolloConfigDB?characterEncoding=utf8
apollo_config_db_username=root
apollo_config_db_password=cmmplb

# apollo portal db info
apollo_portal_db_url=jdbc:mysql://虚拟机ip或者localhost:3306/ApolloPortalDB?characterEncoding=utf8
apollo_portal_db_username=root
apollo_portal_db_password=cmmplb

...
# meta server url
config_server_url=http://虚拟机ip:8080
admin_server_url=http://虚拟机ip:8090
eureka_service_url=$config_server_url/eureka/
portal_url=http://虚拟机ip:8070

-----------------------------------------------------------------------------
进入到/apollo-build-scripts-master
./demo.sh start
Config service:http://192.168.178.110:8080    给应用端调用
Portal service:http://192.168.178.110:8070
Admin Service:为了给Portal service 调用


