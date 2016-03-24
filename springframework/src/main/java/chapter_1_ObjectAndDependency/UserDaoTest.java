package chapter_1_ObjectAndDependency;

import java.sql.SQLException;

import chapter_1_ObjectAndDependency.dao.DaoFactory;
import chapter_1_ObjectAndDependency.dao.UserDao;
import chapter_1_ObjectAndDependency.domain.User;

public class UserDaoTest {
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
		
		
		UserDao dao = new DaoFactory().userDao();
		
		User user = new User();
		user.setId("Kyle2");
		user.setName("Hee2");
		user.setPassword("11112");
		
		dao.add(user);
		
		User user2 = dao.get(user.getId());
		
		System.out.println(user2.getName());
	
	}
}
