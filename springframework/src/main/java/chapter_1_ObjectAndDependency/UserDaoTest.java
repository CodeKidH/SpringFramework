package chapter_1_ObjectAndDependency;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import chapter_1_ObjectAndDependency.dao.CountingConnectionMaker;
import chapter_1_ObjectAndDependency.dao.UserDao;
import chapter_1_ObjectAndDependency.domain.User;

public class UserDaoTest {
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
		
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User();
		user.setId("Kyle2");
		user.setName("Hee2");
		user.setPassword("11112");
		
		dao.add(user);
		
		User user2 = dao.get(user.getId());
		
		System.out.println(user2.getName());
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
		System.out.println("count:"+ccm.getCount());
	}
}
