package chapter_2_Test.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import chapter_2_Test.dao.UserDao;
import chapter_2_Test.domain.User;

public class UserDaoTest {
	
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before
	public void setUp(){
		ApplicationContext context = new GenericXmlApplicationContext("chapter_2_Test/dao/applicationContext.xml");
		
		this.dao = context.getBean("userDao",UserDao.class);
		
		this.user1 = new User("James","hee","spring");
		this.user2 = new User("Kyle","jeong","spring1");
		this.user3 = new User("Tom","min","spring2");
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{	
		
		
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
		
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown");
	}
}
