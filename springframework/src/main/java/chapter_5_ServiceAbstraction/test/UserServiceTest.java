package chapter_5_ServiceAbstraction.test;

import static chapter_5_ServiceAbstraction.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static chapter_5_ServiceAbstraction.service.UserService.MIN_RECCOMEND_FOR_SILVER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

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
					new User("kyle","jeong","p1",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER, 0),
					new User("kyle1","jeong1","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER, 0),
					new User("kyle2","jeong2","p3",Level.SILVER,60, 29),
					new User("kyle3","jeong3","p4",Level.SILVER,60, MIN_RECCOMEND_FOR_SILVER),
					new User("kyle4","jeong4","p5",Level.GOLD,100, MIN_RECCOMEND_FOR_SILVER)
				);
	}
	
	@Test
	public void upgradeLevels(){
		
		userDao.deleteAll();
		
		for(User user : users)
			userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgrade(users.get(0), false);
		checkLevelUpgrade(users.get(1), true);
		checkLevelUpgrade(users.get(2), false);
		checkLevelUpgrade(users.get(3), true);
		checkLevelUpgrade(users.get(4), false);
	}
	
	private void checkLevelUpgrade(User user, boolean upgrade){ //boolean will check, It is possible to update level
		User userUpdate = userDao.get(user.getId());
		
		if(upgrade){
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel())); // update
		}else{
			assertThat(userUpdate.getLevel(), is(user.getLevel()));// not update
		}
		
	}
	
	@Test
	public void add(){
		
		userDao.deleteAll();
		
		User userWithLevel = users.get(4); //It has a already GOLD LEVEL 
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		//Get datas from DB
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(),is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(),is(Level.BASIC));
		
	}
	
	@Test
	public void upgradeAllOrNothing(){
		
		UserService testUserService = new TestUserService(users.get(3).getId());
		
		testUserService.setUserDao(this.userDao); //hand-operated DI
		
		userDao.deleteAll();
		for(User user:users)userDao.add(user);
		
		try{
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected"); //Exception occur
		}catch(TestUserServiceException e){
			
		}
		
		checkLevelUpgrade(users.get(1), false); //Check a level status 
	}
	
	static class TestUserService extends UserService{
		
		private String id;
		
		private TestUserService(String id){ //This Id will make a exception
			this.id = id;
		}
		
		protected void upgradeLevel(User user){
			if(user.getId().equals(this.id))throw new TestUserServiceException();
			super.upgradeLevels();
		}
	}

	static class TestUserServiceException extends RuntimeException{
		
	}
}


