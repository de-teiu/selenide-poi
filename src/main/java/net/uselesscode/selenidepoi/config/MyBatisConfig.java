package net.uselesscode.selenidepoi.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("net.uselesscode.selenidepoi.mapper")
public class MyBatisConfig {

	@Autowired
	DataSource dataSource;

	// org.mybatis.spring.SqlSessionFactoryBeanをBean定義します。
	// これによりSqlSessionFactoryBeanを利用してSqlSessionFactoryが生成されます。
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		// データソースを設定する。MyBatisの処理の中でSQLを発行すると、
		// ここで指定したデータソースからコネクションが取得されます。
		sessionFactoryBean.setDataSource(dataSource);
		// MyBatis設定ファイルを指定します。
		// 今回はresources直下に設定ファイルを配置します。
		sessionFactoryBean.setConfigLocation(new ClassPathResource("/myBatis-config.xml"));
		return sessionFactoryBean;
	}

	// トランザクションマネージャーのBeanを定義します。
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
}
