package chapter_1_ObjectAndDependency.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration // It means DaoFactory is setting information which will be used by ApplicationContext
public class CountingDaoFactory {
	
	@Bean //It will be in charge of creating object
	public UserDao userDao(){
		
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());
		return userDao;
	}
	
	@Bean
	public ConnectionMaker connectionMaker(){
		
		CountingConnectionMaker connect = new CountingConnectionMaker()	;
		connect.setCountingConnectionMaker(realConnectionMaker());
		return connect;
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker(){
		return new NConnectionMaker();
	}
	
	@Bean
	public DataSource dataSource(){
		
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/test");
		dataSource.setUsername("root");
		dataSource.setPassword("1111");
		
		return dataSource;
	}

}
