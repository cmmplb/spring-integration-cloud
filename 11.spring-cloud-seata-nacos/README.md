
#### 解压seata-server,conf目录file.conf文件，更改mode类型为db，db数据库配置账号密码

````
  store {
    
    mode = "db"
    ....

  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp)/HikariDataSource(hikari) etc.
    datasource = "druid"
    ## mysql/oracle/postgresql/h2/oceanbase etc.
    dbType = "mysql"
    driverClassName = "com.mysql.jdbc.Driver"
    ## if using mysql to store the data, recommend add rewriteBatchedStatements=true in jdbc connection param
    ## 如果要是没有sata数据库，要建立一个
    url = "jdbc:mysql://127.0.0.1:3306/seata?rewriteBatchedStatements=true"
    user = "root"
    password = "cmmplb"
    minConn = 5
    maxConn = 100
    ## 建好数据库之后新建下面的这三张表
    globalTable = "global_table"
    branchTable = "branch_table"
    lockTable = "lock_table"
    queryLimit = 100
    maxWait = 5000
  }
````

####修改 conf/registry.conf 配置，将 type 改为 nacos

````
registry {
  type = "nacos"
 
  nacos {
    application = "seata-server"
    serverAddr = "127.0.0.1:8848"
    group = "SEATA_GROUP"
    namespace = "public"
    cluster = "default"
    username = "nacos"
    password = "nacos"
  }
}
 
config {
  type = "nacos"
 
  nacos {
    serverAddr = "127.0.0.1:8848"
    namespace = "public"
    group = "SEATA_GROUP"
    username = "nacos"
    password = "nacos"
    dataId = "seataServer.properties"
  }
}
````

####修改 conf/nacos-config.txt配置
