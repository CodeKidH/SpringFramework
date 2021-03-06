# Test
<hr>

    Main value of Spring is OOP and Test

## 1. Review UserDaoTest.class


#### 1_1 Usefulness of Test

* UserDaoTest.class
     - We have to test our code after our work is done
     
     
~~~java
public class UserDaoTest {
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
		
		
		ApplicationContext context = new GenericXmlApplicationContext("chapter_1_ObjectAndDependency/dao/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User();
		user.setId("Kyle2");
		user.setName("Hee2");
		user.setPassword("11112");
		
		dao.add(user);
		
		User user2 = dao.get(user.getId());
		
		System.out.println(user2.getName());
		
	}
}
~~~

#### 1_2. Characteristics of UserDaoTest

        1. UserDaoTest use a main() to make it easier for test
        2. Target of Test(UserDao) was requested by UserDaoTest
        
   
   * Unit Test
   
        - If we have a specific test part, We will focus on it

#### 1_3. Problem of UserDaoTest

        1.  Inconvenience of checking 
        2.  Inconvenience of Executing


## 2. To improve a UserDaoTest

#### 2_1. Test automation

 
* before

~~~java
	System.out.println(user2.getName());
	System.out.println(user2.getPassword());
~~~

* after
~~~java
		if(!user.getName().equals(user2.getName())){
			System.out.println("fail(name)");
		}else if(!user.getPassword().equals(user2.getPassword())){
			System.out.println("fail(pw)");
		}else{
			System.out.println("success");
		}
~~~

#### 2_2. Efficient execution and manage

* Junit Test

	-  Required condition
	
		1. Method's name must have a public
		2. The method contain a @Test

~~~java
@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{	
		ApplicationContext context = new 				    GenericXmlApplicationContext("chapter_1_ObjectAndDependency/dao/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User();
		user.setId("Kyle2");
		user.setName("Hee2");
		user.setPassword("11112");
		
		dao.add(user);
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(),is(user.getName()));
		assertThat(user2.getPassword(),is(user.getPassword()));
		
	}
~~~

* Junit Execution

~~~java
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
		
		JUnitCore.main("chapter_2_Test.test.UserDaoTest");
	}
~~~

## 3. Junit for developer

#### 3_1. How to execute Junit

* Eclipse


	~~~java
	1. [Run as] -> [Junit Test]
	2. Alt + Shift + x + t
	~~~

#### 3_2. Consistency of test result

* To add deleteAll() and getCount()
	- UserDao.class - deleteAll(), getCount()
	
~~~java
	public void deleteAll() throws SQLException{
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("delete from users");
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}

	public int getCount() throws SQLException{
		
		Connection c= dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		int count = rs.getInt(1);
		
		rs.close();
		ps.close();
		c.close();
		
		return count;
		
	}
~~~

* Test of deleteAll() and getCount()
	- UserDaoTest.class
	
	~~~java
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{	
		ApplicationContext context = new GenericXmlApplicationContext("chapter_2_Test/dao/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		//add
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		User user = new User();
		user.setId("Kyle2");
		user.setName("Hee2");
		user.setPassword("11112");

		//add
		dao.add(user);
		assertThat(dao.getCount(),is(1));
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(),is(user.getName()));
		assertThat(user2.getPassword(),is(user.getPassword()));
		
	}
	~~~

#### 3_3. Comprehensive test

* getCount() test

	- User.class
	~~~java
		public class User {
			
			String id;
			String name;
			String password;
		
			public User(String id, String name, String password){
				this.id = id;
				this.name = name;
				this.password = password;
			}
			
			public User(){
				
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
	- UserDaoTest.class
	~~~java
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
	~~~

* To improve a addAndGet() test

~~~java
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
~~~

* Exception test for get()

		What if value of ID is null?
		1. It would return null
		2. Exception occur
	
	- UserDaoTest.class
	~~~java
	@Test(expected=EmptyResultDataAccessException.class)//Specify a class which exception occur
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		
		ApplicationContext context = new GenericXmlApplicationContext("chapter_2_Test/dao/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown");
	}
	~~~

	- expected
		1. If @Test has a expected, it will return fail when you pass the test
	

* Modify a code to succeed test	

	- UserDao.class - get()
	
	~~~java
	public User get(String id)throws ClassNotFoundException, SQLException{
		
		
		this.c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1,id);
		
		ResultSet rs = ps.executeQuery();
		
		User user = null;
		
		if(rs.next()){
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		rs.close();
		ps.close();
		c.close();
		
		if(user == null){
			throw new EmptyResultDataAccessException(1);
		}
		
		return user;
		
	}
	~~~

#### 3_4. Test leads development

* To think of get() Exception test development

~~~java
	1. Normal development process
		UserDao modify ------> Test

	2. In get() Exception case
		Test -------> UserDao modify
~~~

* TDD(Test driven development)


		TDD : We don't make a code which was failed in testing

#### 3_5 Improving Test code
	
	Repeated code removal in Test Code


* @Before
	- @Before is provided by Junit
	- It will be started first, before @Test is started 
	
	
	- UserDaoTest.class
	~~~java
	private UserDao dao;
	
	@Before
	public void setUp(){
		ApplicationContext context = new GenericXmlApplicationContext("chapter_2_Test/dao/applicationContext.xml");
		
		this.dao = context.getBean("userDao",UserDao.class);
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{	
		
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
		
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown");
	}
	~~~
	
	- Junit Simple Process
	~~~java
		1. Junit search for all of the method attached @Test
		
		2. Junit make a Object to test a test method
		
		3. @Before method is started
		
		4. Junit call a one @Test and save it
		
		5. @After method is started
		
		6. Repeat a process from 2 to 5
		
		7. Junit hand all of the test over to us
		

		Junit -------------------->  @Test
			(New Object)
		      -------------------->  @Test
		      	(New Object)
		      	.
		      	.
		      	.


	~~~

* fixture

		fixture : We need information or objects to test, These information or objects we call it fixture
	
		dao == fixture
		User's object == fixture
	

	- UserDaoTest.class
	
	~~~java
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
	
	~~~

## 4. Spring test

####4_1. Testing for applicationContext

* Test contextframework of spring

	- UserDaoTest.class
	~~~java
	@RunWith(SpringJUnit4ClassRunner.class)// Junit's function of extension 
	@ContextConfiguration(locations="/chapter_2_Test/dao/applicationContext.xml")// Define a location of applicationContext
	public class UserDaoTest {
	
	@Autowired 
	private ApplicationContext context;
	
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before	
	public void setUp(){
		
		this.dao = this.context.getBean("userDao",UserDao.class);
		
		this.user1 = new User("James","hee","spring");
		this.user2 = new User("Kyle","jeong","spring1");
		this.user3 = new User("Tom","min","spring2");
	}
	~~~

* Context Sharing of test method

	- To check
	~~~java
	@Before
	public void setUp(){
		System.out.println(this.context);
		System.out.println(this);
	}
	~~~
	
	- Result
	~~~java
	.org.springframework.context.support.GenericApplicationContext@3498ed: 
	chapter_2_Test.test.UserDaoTest@17c1bced
	.org.springframework.context.support.GenericApplicationContext@3498ed: 
	chapter_2_Test.test.UserDaoTest@5223e5ee
	.org.springframework.context.support.GenericApplicationContext@3498ed: 
	chapter_2_Test.test.UserDaoTest@636be97
	~~~
	
	- All of the Context objects is same
	- Objects of UserDaoTest is different
	- Execution speed increase(0.23, 0.10,0.07)
	
* Context sharing of test class

		Spring provide context sharing for classes
		
	~~~java
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations="/chapter_2_Test/dao/applicationContext.xml")
	public class UserDaoTest {
	
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations="/chapter_2_Test/dao/applicationContext.xml")
	public class GroupDaoTest {
	~~~

* Autowired

		@Autowired can be used to DI
		
		1. Context framework search for bean which same name in Context
		2. We don't need to use a getBean() or methods

	- UserDaoTest.class
	~~~java
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations="/chapter_2_Test/dao/applicationContext.xml")
	public class UserDaoTest {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before	
	public void setUp(){
		
		
		this.user1 = new User("James","hee","spring");
		this.user2 = new User("Kyle","jeong","spring1");
		this.user3 = new User("Tom","min","spring2");
		
		System.out.println(this.context);
		System.out.println(this);
	}
	~~~
	- UserDao.class
	~~~java
	public class UserDao {
	
	@Autowired
	private DataSource dataSource;
	private Connection c;
	private User user;
	
	
	public void add(User user)throws ClassNotFoundException, SQLException{
		
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1,user.getId());
		ps.setString(2,user.getName());
		ps.setString(3,user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
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
	
	<bean id="userDao" class="chapter_2_Test.dao.UserDao"/>
	
	</beans>
	~~~

#### 4_2 DI and Test
	

	We can make a test DataSource in the test code
	

* DI by test code
 	- UserDaoTest.class
	
	~~~java
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations="/chapter_2_Test/dao/applicationContext.xml")
	@DirtiesContext
	public class UserDaoTest {
	
		@Autowired
		private ApplicationContext context;
		
		@Autowired
		UserDao dao;
		
		private User user1;
		private User user2;
		private User user3;
		
		
		@Before	
		public void setUp(){
			
			
			this.user1 = new User("James","hee","spring");
			this.user2 = new User("Kyle","jeong","spring1");
			this.user3 = new User("Tom","min","spring2");
			
			System.out.println(this.context);
			System.out.println(this);
			
			
			DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/test","root","1111",true);
			dao.setDataSource(dataSource);
		}
		
	~~~


	- UserDao.class

	~~~java
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	~~~

	~~~java
	@DirtiesContext : @DirtiesContext give a modified status of class to Test framework
	~~~

* DI test without container

		It become a simple test
		
	- UserDaoTest.class
	
	~~~java
	public class UserDaoTest {
	
	UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before	
	public void setUp(){
		
		
		this.user1 = new User("James","hee","spring");
		this.user2 = new User("Kyle","jeong","spring1");
		this.user3 = new User("Tom","min","spring2");
		
		
		dao = new UserDao();
		DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/test","root","1111",true);
		dao.setDataSource(dataSource);
	}
	~~~
	
## 5. Spring by learning Test

#### 5_1. Examplt of learning test

* Junit test object

	Junit always make a new Object when they start a @test?
	
	- JunitTest.class
	~~~java
	import static org.hamcrest.CoreMatchers.is;
	import static org.hamcrest.CoreMatchers.not;
	import static org.hamcrest.CoreMatchers.sameInstance;
	import static org.junit.Assert.assertThat;
	
	import org.junit.Test;
	
	public class JunitTest {
		
		static JunitTest testObject;
		
		@Test
		public void test1(){
			assertThat(this, is(not(sameInstance(testObject))));
			testObject = this;
		}
		
		@Test
		public void test2(){
			assertThat(this, is(not(sameInstance(testObject))));
			testObject = this;
		}
		
		@Test
		public void test3(){
			assertThat(this, is(not(sameInstance(testObject))));
			testObject = this;
		}
	
	}
	~~~
	
	~~~java
	
	not() == deny the result 
	is(not()) == It is not same = true
	sameInstance() =  Creates a matcher that matches only when the examined object is the same instance as the specified target 		object.
	
	What if It is same between first object and last object?
	~~~
	
	- Improving JunitTest
	~~~java
	public class JunitTest {
	
		static Set<JunitTest> testObjects = new HashSet<JunitTest>();
		
		@Test
		public void test1(){
			assertThat(testObjects,not(hasItem(this)) );
			testObjects.add(this);
		}
		
		@Test
		public void test2(){
			assertThat(testObjects,not(hasItem(this)) );
			testObjects.add(this);
		}
		
		@Test
		public void test3(){
			assertThat(testObjects,not(hasItem(this)) );
			testObjects.add(this);
		}
	
	}
	~~~
	
	~~~java
	hasItem() ==  Creates a matcher for Iterables that only matches when a single pass over the examined Iterable yields at least 	one item that is equal to the specified item.
	~~~
	
* Spring context test

		Is it true Spring context was made only one in test case?
	
	- junit.xml
	~~~xml
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://www.springframework.org/schema/beans
								http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
		
		
	</beans>
	~~~
	
	- JunitTest.class
	~~~java
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration
	public class JunitTest {
		
		@Autowired
		ApplicationContext context;
		
		static Set<JunitTest> testObjects = new HashSet<JunitTest>();
		static ApplicationContext contextObject = null;
		
		@Test
		public void test1(){
			assertThat(testObjects,not(hasItem(this)) );
			testObjects.add(this);
		}
		
		@Test
		public void test2(){
			assertThat(testObjects,not(hasItem(this)) );
			testObjects.add(this);
		}
		
		@Test
		public void test3(){
			assertThat(testObjects,not(hasItem(this)) );
			testObjects.add(this);
			
			assertThat(contextObject, either(is(nullValue())).or(is(this.contextObject)));
			contextObject = this.context;
		}
	
	}
	~~~
	
