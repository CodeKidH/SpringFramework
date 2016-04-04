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
