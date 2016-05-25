package chapter_5_ServiceAbstraction.test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chapter_5_ServiceAbstraction.dao.UserDao;
import chapter_5_ServiceAbstraction.domain.User;
import chapter_5_ServiceAbstraction.domain.User.Level;
import chapter_5_ServiceAbstraction.service.UserService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/chapter_5_ServiceAbstraction/dao/applicationContext.xml")
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	List<User> users; //test fixture
	
	@Before
	public void setUp(){
		users = Arrays.asList(
					new User("kyle","jeong","p1",Level.BASIC,49, 0),
					new User("kyle1","jeong1","p2",Level.BASIC,50, 0),
					new User("kyle2","jeong2","p3",Level.SILVER,60, 29),
					new User("kyle3","jeong3","p4",Level.SILVER,60, 30),
					new User("kyle4","jeong4","p5",Level.GOLD,100, 100)
				);
	}
	
	@Test
	public void upgradeLevels(){
		
		userDao.deleteAll();
		
		for(User user : users)
			userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0),Level.BASIC);
		checkLevel(users.get(1),Level.SILVER);
		checkLevel(users.get(2),Level.SILVER);
		checkLevel(users.get(3),Level.GOLD);
		checkLevel(users.get(4),Level.GOLD);
	}
	
	private void checkLevel(User user, Level expectedLevel){
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
}
