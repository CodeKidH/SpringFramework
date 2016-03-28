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

* Object Factory's object 

		chapter_1_ObjectAndDependency.dao.UserDao@2a139a55
		chapter_1_ObjectAndDependency.dao.UserDao@15db9742
		== false
	
	
* ApplicationContext's object

		chapter_1_ObjectAndDependency.dao.UserDao@6366ebe0
		chapter_1_ObjectAndDependency.dao.UserDao@6366ebe0
		== true





