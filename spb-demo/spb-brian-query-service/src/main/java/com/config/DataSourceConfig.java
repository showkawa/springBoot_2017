package com.config;

import com.alibaba.druid.pool.DruidDataSource;
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


@Configuration
@MapperScan(basePackages = "com.kawa", sqlSessionFactoryRef = "kawaSqlSessionFactory")
public class DataSourceConfig {

   /* @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }*/

    /**
     * 配置数据源
     * @return
     */
    @Bean(name="kawaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.kawa")
    public DataSource kawaDataSource(){

        return DataSourceBuilder.create().build();
    }

    /**
     * sql会话工厂
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "kawaSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("kawaDataSource") DataSource dataSource)
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
    @Bean(name = "kawaTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("kawaDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "kawaSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("kawaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }



}
