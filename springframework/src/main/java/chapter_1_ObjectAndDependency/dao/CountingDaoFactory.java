package chapter_1_ObjectAndDependency.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // It means DaoFactory is setting information which will be used by ApplicationContext
public class CountingDaoFactory {
	
	@Bean //It will be in charge of creating object
	public UserDao userDao(){
		
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
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

}
