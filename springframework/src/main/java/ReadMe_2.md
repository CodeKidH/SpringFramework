# 5. Spring of IOC

#### 5_1. Spring's IOC by using object factory

* ApplicationContext and configural information

  - Bean : Bean Object is made by Spring and IOC has application to this Object
  - Bean factory(IOC Object) : Bean factory(IOC Object) is in charge of creating bean, configured relation in Spring
  - ApplicationContext : If Bean factory  will be extended, We will call it ApplicationContext and It refers to configural information to create bean

* ApplicationContext that use a DaoFactory

        To make a configural information by using DaoFactory


* DaoFactory.class

~~~java
@Configuration // It means DaoFactory is setting information which will be used by ApplicationContext
public class DaoFactory {
	
	@Bean //It will be in charge of creating object
	public UserDao userDao(){
		return new UserDao(connectionMaker()); 
	}
	
	@Bean
	public ConnectionMaker connectionMaker(){
		
		return new NConnectionMaker();
	}

}
~~~

* UserDaoTest.class

~~~java
public class UserDaoTest {
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
		
		
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//getBean method request object managed by ApplicationContext 
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

#### 5_2. How to operate ApplicationContext

    Object Factory = Spring ApplicationContext = IOC container = Spring Container

* pros of using ApplicationContext
  - Client doesn't need to know specific class of Factory
  - ApplicationContext provide multi IOC service 
  - ApplicationContext give a lot of ways to search for bean

<hr>

# 6. Singleton registry and Object scope

	What is different between Object Factory and ApplicationContext?

* Factory's object 
~~~java
  UserDao dao = new DaoFactory().userDao();
  UserDao dao1 = new DaoFactory().userDao();
  
  System.out.println(dao);
  System.out.println(dao1);
~~~
	- result
		chapter_1_ObjectAndDependency.dao.UserDao@2a139a55
		chapter_1_ObjectAndDependency.dao.UserDao@15db9742
		dao==dao1(false)
	
	
* ApplicationContext's object

~~~java
UserDao dao = context.getBean("userDao",UserDao.class);
UserDao dao1 = context.getBean("userDao",UserDao.class);

System.out.println(dao);
System.out.println(dao1);
~~~
	- result
		chapter_1_ObjectAndDependency.dao.UserDao@6366ebe0
		chapter_1_ObjectAndDependency.dao.UserDao@6366ebe0
		dao==dao1(true)

#### 6_1. ApplicationContext as a Singleton registry

		ApplicationContext is a Singleton registry which manage singleton

* Why does Spring make a bean of singleton?
	- Because Spring usually operates on server

#### 6_2. Singleton and Status of object

		Singleton on the Multithread environment should be stateless 
		Status of stateless have to use a local variable to keep stateless

* UserDao.class

~~~java
public class UserDao {
	
	private ConnectionMaker connectionMaker; 
	
	//If it is worked on Multithread environment, It would raise a problem 
	private Connection c;
	private User user;
	
	public UserDao(ConnectionMaker connectionMaker){
		this.connectionMaker = connectionMaker; 
	}
	
	public void add(User user)throws ClassNotFoundException, SQLException{
		
		Connection c = connectionMaker.makeConnection(); 
		
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1,user.getId());
		ps.setString(2,user.getName());
		ps.setString(3,user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id)throws ClassNotFoundException, SQLException{
		
		
		this.c = connectionMaker.makeConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1,id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		this.user = new User();
		this.user.setId(rs.getString("id"));
		this.user.setName(rs.getString("name"));
		this.user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return this.user;
		
	}
}

~~~

#### 6_3 . Scope of Spring bean

	Object is managed by Spring, We call it bean
	Scope of Bean is adjustment of the scope  which is created
	Spring's basic scope is Singleton
	

<hr>

# 7. DI

#### 7_1. IOC and DI

	Operation principle of IOC Spring is DI
	
#### 7_2. Runtime Dependency Relationship

* Dependency Relationship
	- A --------> B
	- If B was changed, A also will be changed 
	- If A was changed, B doesn't care about it 

* Dependency Relationship of UserDao
	- UserDao ---------> ConnectionMaker(interface) ----implementation----> DConnectionMaker(dependency object)
	- If ConnectionMaker was changed, UserDao also will be changed
	- But If DConnectinMaker was changed, UserDao doesn't care about it Just Use a Connection
	- DI is connection between UserDao(Client) and DConnectionMaker(dependency object) 

* DI
	- DI need a Interface 
	- Dependency Relationship of runtime is decided by something, like Container(IOC Container, DaoFactory, ApplicationContext..)

* DI of UserDao

	- Before Constructor is separated in UserDao.class
	~~~java
	public UserDao(){
		connectionMaker = new DconnectionMaker();
	}
	~~~

	- After we use a DaoFactory
	~~~java
	public class UserDao {
	
		private ConnectionMaker connectionMaker; 
		
		public UserDao(ConnectionMaker connectionMaker){
			this.connectionMaker = connectionMaker; 
		}
	}
	//	UserDao.class <---------------------- DConnectioniMaker.class
	//  <connectionMaker>	        DI
	//        <add()>   -------------------------->makeConnectionMaker()
	//	           Usage Dependency relationship
	~~~

#### 7_3. Dependency lookup and Injection

* Spring provide two ways of IOC
	- DI
	- Dependency lookup

* Dependency lookup


		Creating of object , Using of object was decided by IOC but when It get a object
		It request a container to get a object in person
		Not use a Method or Constructor


#### 7_4 Application example of DI

* To exchange of function
	If we use two DBs, One is a LocalDB, Another one is ProductionDB

	- DaoFactory.class
	
	~~~java
	@Bean
	public ConnectionMaker connectionMaker(){
		return new LocalDBConnectionMaker(); // Using a local
	}
	~~~

	~~~java
	@Bean
	public ConnectionMaker connectionMaker(){
		return new ProductionDBConnectionMaker(); // Using a production
	}
	~~~

* To add a function 

	How many times DAO access to DB?
	Will I add a this logic each DB Connection to know access times? and When this work ends, I delete it every part
	so We use a DI to make work easier

	- CountingConnectionMaker.class
	~~~java
	public class CountingConnectionMaker implements ConnectionMaker{
	
		int count = 0;
		private ConnectionMaker realConnectionMaker;
		
		public CountingConnectionMaker(ConnectionMaker realConnectionMaker){
			this.realConnectionMaker = realConnectionMaker;
		}
		
		public Connection makeConnection()throws ClassNotFoundException, SQLException{
			this.count++;
			return realConnectionMaker.makeConnection();
		}
		
		public int getCount(){
			return this.count;
		}
	}
	~~~
	
	- CountingDaoFactory.class
	~~~java
	@Configuration 
	public class CountingDaoFactory {
		
		@Bean 
		public UserDao userDao(){
			return new UserDao(connectionMaker()); 
		}
		
		@Bean
		public ConnectionMaker connectionMaker(){
			
			return new CountingConnectionMaker(realConnectionMaker());
		}
		
		@Bean
		public ConnectionMaker realConnectionMaker(){
			return new NConnectionMaker();
		}
	
	}
	~~~
	
	- Add Before
	~~~java
		UserDao --------------> NConnection
	~~~
	- Add After
	~~~java
		UserDao --------------> CountingConnectionMaker -------------> NConnection
	~~~
	
	- UserDaoTest.class
	
	~~~java
	public class UserDaoTest {
		public static void main(String[]args)throws ClassNotFoundException, SQLException{
			
			//I can get any Bean by using DL
			CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
			System.out.println("count:"+ccm.getCount());
		}
	}
	~~~

#### 7_5 To DI by using a method
	
	We have two ways of DI by Using mehtod
	1. Using Normal Method
	2. Using Setter Method

* Setter Method

	- Method's name always start 'set'
	- Setter Method saves a outside object reference and then inner method can use it
	- It has a only one parameter
	
* Normal Mehtod
	
	- It has a lot of parameter

* Using
	- UserDao.class
	~~~java
	public void setConnectionMaker(ConnectionMaker connectionMaker){
		this.connectionMaker = connectionMaker; 
	}
	~~~

	- CountingDaoFactory.class
	~~~java
	@Bean 
	public UserDao userDao(){
		
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
		return userDao;
	}
	~~~

# 8. To use a XML 

	When we use a Di, XML is more accessible DI than Java 

#### 8_1 XML config

	~~~java
	@Configuration -----------------> <beans>
	@Bean methodname ---------------> <bean id="methodName">
	@return new BeanClass() --------> class="a,b,c...BeanClass"
	~~~

* connectionMaker() conversion
	
	~~~java
	@Bean
	public ConnectionMaker connectionMaker(){
		CountingConnectionMaker connect = new CountingConnectionMaker()	;
		connect.setCountingConnectionMaker(realConnectionMaker()); 
	
		setCountingConnectionMaker-----> <property name="countingConnectionMaker"  
		realConnectionMaker() ----------> ref ="realConnectionMaker"/>

		return connect;
	}
	
	@Bean --------------------------------------> <bean
	public ConnectionMaker realConnectionMaker(){ --------------> id="realConnectionMaker"
		return new NConnectionMaker(); -----------> class="NConnectionMaker"
	}
	~~~
	
	~~~xml
	<bean id="connectionMaker" class="chapter_1_ObjectAndDependency.dao.CountingConnectionMaker">
		<property name="countingConnectionMaker" ref="realConnectionMaker"/>
	</bean>
	
	<bean id="realConnectionMaker" class="chapter_1_ObjectAndDependency.dao.NConnectionMaker">
	</bean>
	~~~

* UserDao conversion

	~~~java
	@Bean 
	public UserDao userDao(){
		
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
		return userDao;
	}
	~~~
	
	~~~xml
	<bean id="userDao" class="chapter_1_ObjectAndDependency.dao.UserDao">
		<property name="connectionMaker" ref="connectionMaker">
	</bean>
	~~~

#### 8_2. To use a ApplicationContext.xml


* applicationContext.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.springframework.org/schema/beans
								http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	
	<bean id="userDao" class="chapter_1_ObjectAndDependency.dao.UserDao">
		<property name="connectionMaker" ref="connectionMaker"/>
	</bean>
	
	<bean id="connectionMaker" class="chapter_1_ObjectAndDependency.dao.CountingConnectionMaker">
		<property name="countingConnectionMaker" ref="realConnectionMaker"/>
	</bean>
	
	<bean id="realConnectionMaker" class="chapter_1_ObjectAndDependency.dao.NConnectionMaker">
	</bean>
	
</beans>
			
~~~

* UserDaoTest.class
~~~java

	ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
~~~

* Kind of xml
  - We also use a ClassPathXmlApplicationContext
  - Path of applicationContext.xml

~~~java
	src - main - java - chapter1 - dao - applicationContext.xml
					     - UserDao.class
					     - .....
	
	1. GenericXmlApplicationContext
		new GenericXmlApplicationContext("chapter1.dao.applicationContext.xml")

	2. ClassPathXmlApplicationContext
		new ClassPathXmlApplicationContext("applicationContext.xml",UserDao.class)
~~~
	

