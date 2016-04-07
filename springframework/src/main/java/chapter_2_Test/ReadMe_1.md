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

	

