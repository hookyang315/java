package cn.sdframework.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("cn.sdframework")
@EnableTransactionManagement
public class SdConfig {

	@Bean(autowire = Autowire.BY_TYPE, destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(ApplicationParam.driverClass);
		dataSource.setUrl(ApplicationParam.url);
		dataSource.setUsername(ApplicationParam.username);
		dataSource.setPassword(ApplicationParam.password);
		dataSource.setInitialSize(Integer.parseInt(ApplicationParam.initialSize));
		dataSource.setMaxActive(Integer.parseInt(ApplicationParam.maxActive));
		dataSource.setMaxIdle(Integer.parseInt(ApplicationParam.maxIdle));
		dataSource.setMaxWait(Integer.parseInt(ApplicationParam.maxWait));
		dataSource.setMinIdle(Integer.parseInt(ApplicationParam.minIdle));
		if (ApplicationParam.dbType.equals("ORACLE")) {
			dataSource.setValidationQuery("select 1 from dual");
		} else {
			dataSource.setValidationQuery("SELECT 1");
		}
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(false);
		return dataSource;
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate() {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource());
		return jdbcTemplate;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager() throws Exception {
		return new DataSourceTransactionManager(dataSource());
	}
}
