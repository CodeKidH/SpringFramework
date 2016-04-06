package chapter_2_Test.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import chapter_2_Test.dao.UserDao;
import chapter_2_Test.domain.User;

public class UserDaoTest {
	
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{	
		ApplicationContext context = new GenericXmlApplicationContext("chapter_2_Test/dao/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		User user1 = new User("James","hee","spring");
		User user2 = new User("Kyle","jeong","spring1");
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getId(),is(user1.getId()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getId(),is(user2.getId()));
	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException{
		
		ApplicationContext context = new GenericXmlApplicationContext("chapter_2_Test/dao/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user1 = new User("James","hee","spring");
		User user2 = new User("James1","hee1","spring1");
		User user3 = new User("James2","hee2","spring2");
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(),is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		

		dao.add(user3);
		assertThat(dao.getCount(),is(3));
	}
	
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
		
		JUnitCore.main("chapter_2_Test.test.UserDaoTest");
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		
		ApplicationContext context = new GenericXmlApplicationContext("chapter_2_Test/dao/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown");
	}
}
