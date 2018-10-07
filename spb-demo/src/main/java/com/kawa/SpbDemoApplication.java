package com.kawa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;


/**
 * 1.@SpringBootApplication => SpringBoot的主配置类，运行这个类的main方法来启动SpringBoot应用
 * 		1.1 @SpringBootConfiguration  =>表示spring boot的配置类
 * 		1.2 @EnableAutoConfiguration =>开启自动配置功能
 * 			1.2.1 @AutoConfigurationPackage => 自动配置包
 * 			1.2.2 @Import(EnableAutoConfigurationImportSelector.class)
 * 				=> 	Spring底层注解@Import给容器中导入一个组件，将主配置类（SpringBootApplication）的所在的
 * 					包及下面所有子包里面的所有组件扫描到Spring容器
 *
 *
 */
@SpringBootApplication
public class SpbDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpbDemoApplication.class, args);
	}
	
	 @Autowired
	    private Environment env;
	 	/**
	 	 *destroy-method="close"的作用是当数据库连接不使用的时候,
	 	 *				就把该连接重新放到数据池中,方便下次使用调用. 	
	 	 * @return
	 	 */
	    /*@Bean(destroyMethod =  "close")
	    public DataSource dataSource() {
	        DruidDataSource dataSource = new DruidDataSource();
	        dataSource.setUrl(env.getProperty("spring.datasource.url"));
	        dataSource.setUsername(env.getProperty("spring.datasource.username"));
	        dataSource.setPassword(env.getProperty("spring.datasource.password"));
	        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	        //初始化时建立物理连接的个数
	        dataSource.setInitialSize(2);
	        //最大连接池数量
	        dataSource.setMaxActive(20);
	        //最小连接池数量
	        dataSource.setMinIdle(0);
	        //获取连接时最大等待时间，单位毫秒。
	        dataSource.setMaxWait(60000);
	        //用来检测连接是否有效的sql
	        dataSource.setValidationQuery("SELECT 1");
	        //申请连接时执行validationQuery检测连接是否有效
	        dataSource.setTestOnBorrow(false);
	        //建议配置为true，不影响性能，并且保证安全性。
	        dataSource.setTestWhileIdle(true);
	        //是否缓存preparedStatement，也就是PSCache
	        dataSource.setPoolPreparedStatements(false);
	        return dataSource;
	    }*/
	
	
}
