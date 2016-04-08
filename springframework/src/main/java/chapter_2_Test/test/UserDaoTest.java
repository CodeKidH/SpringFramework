package chapter_2_Test.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chapter_2_Test.dao.UserDao;
import chapter_2_Test.domain.User;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/chapter_2_Test/dao/applicationContext.xml")
//@DirtiesContext
public class UserDaoTest {
	
	/*@Autowired
	private ApplicationContext context;*/
	
	//@Autowired
	UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before	
	public void setUp(){
		
		
		this.user1 = new User("James","hee","spring");
		this.user2 = new User("Kyle","jeong","spring1");
		this.user3 = new User("Tom","min","spring2");
		
		//System.out.println(this.context);
		//System.out.println(this);
		
		dao = new UserDao();
		DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/test","root","1111",true);
		dao.setDataSource(dataSource);
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
