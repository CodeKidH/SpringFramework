package chapter_4_Exception.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chapter_4_Exception.dao.UserDao;
import chapter_4_Exception.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/chapter_4_Exception/dao/applicationContext.xml")
///@DirtiesContext
public class UserDaoTest {
	
	@Autowired
	private ApplicationContext context;
	
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp(){
		this.dao = this.context.getBean("userDao", UserDao.class);
		
		this.user3 = new User("x","x","x");
		this.user1 = new User("v","v","v");
		this.user2 = new User("z","z","z");
	}
	
	@Test
	public void getAll() throws SQLException, ClassNotFoundException{
		
		dao.deleteAll();
		
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(),is(0));
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(),is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);
		List<User>users2 = dao.getAll();
		assertThat(users2.size(),is(2));
		checkSameUser(user1,users2.get(0));
		checkSameUser(user2,users2.get(1));
		
		dao.add(user3);
		List<User>users3 = dao.getAll();
		assertThat(users3.size(),is(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user3, users3.get(1));
		checkSameUser(user3, users3.get(2));
		
	}
	
	
	private void checkSameUser(User user1, User user2){
		assertThat(user1.getId(),is(user2.getId()));
		assertThat(user1.getName(),is(user2.getName()));
		assertThat(user1.getPassword(),is(user2.getPassword()));
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
		
		JUnitCore.main("chapter_4_Exception.test.UserDaoTest");
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown");
	}
}
