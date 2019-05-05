package com.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
//@MapperScan(basePackages = "com.kawa2", sqlSessionFactoryRef = "kawa02SqlSessionFactory")
@MapperScan(basePackages = "com.kawa2", sqlSessionTemplateRef = "kawa02SqlSessionTemplate")
public class DataSourceConfig02 {

   /* @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }*/

    /**
     * 配置数据源
     * @return
     */
    /*@Bean(name="kawa02DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.kawa02")
    public DataSource kawa02DataSource(){

        return DataSourceBuilder.create().build();
    }*/
    @Bean(name="kawa02DataSource")
    public DataSource kawa02DataSource(DbConfig02 dbConfig) throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(dbConfig.getJdbcUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(dbConfig.getPassword());
        mysqlXaDataSource.setUser(dbConfig.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        //将本地事务注册到Atomikos全局事务中
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("kawa02DataSource");

        xaDataSource.setMinPoolSize(dbConfig.getMinPoolSize());
        xaDataSource.setMaxPoolSize(dbConfig.getMaxPoolSize());
        xaDataSource.setMaxLifetime(dbConfig.getMaxLifetime());
        xaDataSource.setBorrowConnectionTimeout(dbConfig.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(dbConfig.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(dbConfig.getMaintenanceInterval());
        xaDataSource.setMaxIdleTime(dbConfig.getMaxIdleTime());
        xaDataSource.setTestQuery(dbConfig.getTestQuery());
        return xaDataSource;
    }

    /**
     * sql会话工厂
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "kawa02SqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("kawa02DataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    /**
     * 事务管理
     * @param dataSource
     * @return
     */
    //事务由Atomikos管理，所以不用声明事务管理
    /*@Bean(name = "kawa02TransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("kawa02DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }*/

    @Bean(name = "kawa02SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("kawa02SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
