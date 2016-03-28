## 5. Spring of IOC

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

## 6. Singleton registry and Object scope

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



