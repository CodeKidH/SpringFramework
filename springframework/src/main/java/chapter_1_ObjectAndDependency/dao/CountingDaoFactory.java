package chapter_1_ObjectAndDependency.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // It means DaoFactory is setting information which will be used by ApplicationContext
public class CountingDaoFactory {
	
	@Bean //It will be in charge of creating object
	public UserDao userDao(){
		return new UserDao(connectionMaker()); 
	}
	
	@Bean
	public ConnectionMaker connectionMaker(){
		
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker(){
		return new NConnectionMaker();
	}

}
