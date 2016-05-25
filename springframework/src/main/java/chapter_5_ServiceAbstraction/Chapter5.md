# Service Abstraction
  
    To apply transaction to DAO
  

## 1. To add a level of user

    List of business logic that will be added
      1) User's level BASIC, SILVER, GOLD
      2) User is BASIC When User sign up for program and later, User will be updated
      3) By login time 50 and up, Level will be SILVER
      4) Level is SILVER and When User get a recommedation 30 times, User is GOLD
      5) This program will execute in a batch
  

#### 1_1. Add a Field

* Level enum - User.java

~~~java
package chapter_5_ServiceAbstraction.domain;

public class User {
	
	
	public enum Level{
		
		BASIC(1), SILVER(2), GOLD(3);
		
		private final int value;
		
		Level(int value){
			this.value = value;
		}
		
		public int intValue(){
			return value;
		}
		
		public static Level valuOf(int value){
			switch(value){
				case 1: return BASIC;
				case 2: return SILVER;
				case 3: return GOLD;
				default : throw new AssertionError("Unknown value:"+value);
			}
		}
	}
	
	String id;
	String name;
	String password;
	Level level;
	int login;
	int recommend;
	

	public User(String id, String name, String password){
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	public User(){
		
	}
	
	
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 
}


~~~
    
    On the inside It has a int, but on the outside It has a object so You can use it safely

* To modify the UserDaoTest
	- UserDaoTest.java
	~~~java
	@Before
	public void setUp(){
		
		this.user3 = new User("x","x","x",Level.BASIC, 1, 0);
		this.user1 = new User("v","v","v",Level.SILVER, 55, 10);
		this.user2 = new User("z","z","z",Level.GOLD, 100, 40);
	}

	private void checkSameUser(User user1, User user2){
		assertThat(user1.getId(),is(user2.getId()));
		assertThat(user1.getName(),is(user2.getName()));
		assertThat(user1.getPassword(),is(user2.getPassword()));
		assertThat(user1.getLevel(),is(user2.getLevel()));
		assertThat(user1.getLogin(),is(user2.getLogin()));
		assertThat(user1.getRecommend(),is(user2.getRecommend()));
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{	
		
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
	}
	~~~
	
	- User.java
	~~~java
	public User(String id, String name, String password, Level level, int login, int recommend){
		this.id = id;
		this.name = name;
		this.password = password;
		this.level = level;
		this.login = login;
		this.recommend = recommend;
	}
	~~~

* UserDaoJDBC

~~~java
package chapter_5_ServiceAbstraction.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import chapter_5_ServiceAbstraction.domain.User;
import chapter_5_ServiceAbstraction.domain.User.Level;

public class UserDaoJdbc implements UserDao{
	
	
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
		public User mapRow(ResultSet rs, int rowNum)throws SQLException{
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		}
	};
	
	public void setDataSource(DataSource dataSource){
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(final User user){
		
		this.jdbcTemplate.update("insert into users(id,name,password, level, login, recommend) values(?,?,?,?,?,?)",
				user.getId(),user.getName(),user.getPassword(),user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}
	
	public List<User> getAll(){
		
		return this.jdbcTemplate.query("select * from users order by id",
				this.userMapper);
	}
	
	public User get(String id){
		
		return this.jdbcTemplate.queryForObject("select * from users where id=?",new Object[]{id},
				this.userMapper);
		
	}
	
	public void deleteAll(){
		
		this.jdbcTemplate.update("delete from users");
		
	}
	
	
	public int getCount(){
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

}

~~~


#### 1_2. To add a Modify function 
 
* To add the function to @Test
~~~java
	@Test
	public void update(){
		dao.deleteAll();
		
		dao.add(user1);
		
		user1.setName("KYLE");
		user1.setPassword("spring");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		
		
	}
~~~

* UserDao and UserDaoJdbc
	- UserDao
	~~~java
	
	public interface UserDao {
		
		void add(User user);
		User get(String id);
		List<User> getAll();
		void deleteAll();
		int getCount();
		public void update(User user);
	}
	~~~
	
	- UserDaoJdbc
	~~~java
	public void update(User user){
		this.jdbcTemplate.update(
					"update users set name = ?, password = ?, level = ?, login = ?," +
						"recommend = ? where id = ?", user.getName(), user.getPassword(),
						user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
						user.getId()
				);
	}
	~~~

* To improve the Update test


	If There is a no where statement in update?
	The test will be success so we need a perfect test


	- @Test
	~~~java
	@Test
	public void update(){
		dao.deleteAll();
		
		dao.add(user1); // It will be modified
		dao.add(user2); // It will not be modified
		
		user1.setName("KYLE");
		user1.setPassword("spring");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
		
		
	}
	~~~


#### 1_3. UserService.upgradeLevels()

	0. Make a Service
	1. Invoke a getAll()
	2. It will execute the upgrade function by each user
	3. Update()


   ![Exception]
(https://raw.githubusercontent.com/KyleJeong/SpringFramework/master/springframework/src/main/java/chapter_5_ServiceAbstraction/images/userService.png)

* UserServiceClass and Bean

	- UserService.java
	~~~java
	public class UserService {
		
		UserDao userDao;
		
		public void setUserDao(UserDao userDao){
			this.userDao = userDao;
		}
	
	}
	~~~
	
	- applicationContext.xml
	~~~xml
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://www.springframework.org/schema/beans
									http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
		
		<bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
			<property name="driverClass" value="com.mysql.jdbc.Driver"/>
			<property name="url" value="jdbc:mysql://localhost/test"/>
			<property name="username" value="root"/>
			<property name="password" value="1111"/>
		</bean>
		
		<bean id="userService" class="chapter_5_ServiceAbstraction.service.UserService">
			<property name="userDao" ref="userDao"/>
		</bean>
		
		<bean id="userDao" class="chapter_5_ServiceAbstraction.dao.UserDaoJdbc">
			<property name="dataSource" ref="dataSource"/>
		</bean>
		
	</beans>
	~~~

* UserServiceTest

	- UserServiceTest.java
	~~~java
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations="/chapter_5_ServiceAbstraction/dao/applicationContext.xml")
	public class UserServiceTest {
		
		@Autowired
		UserService userService;
	}
	~~~
	
	- UserService.java
	~~~java

	public class UserService {
		
		UserDao userDao;
		
		public void setUserDao(UserDao userDao){
			this.userDao = userDao;
		}
		
		public void upgradeLevels(){
			
			List<User> users = userDao.getAll();
			for(User user : users){
				
				Boolean changed = null;
				if(user.getLevel() == Level.BASIC && user.getLogin() >= 50){
					user.setLevel(Level.SILVER);
					changed = true;
				}else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
					user.setLevel(Level.GOLD);
					changed = true;
				}else if(user.getLevel() == Level.GOLD){
					changed = false;
				}else{
					changed = false;
					
				}
				
				if(changed){
					userDao.update(user);
				}
				
			}
			
		}
	
	}

	~~~
	
	- upgradeLevels() Test
	~~~java
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

	~~~
	
	
