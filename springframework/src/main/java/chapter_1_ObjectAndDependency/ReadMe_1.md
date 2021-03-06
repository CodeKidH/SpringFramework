#ObjectAndDependency

<hr>

#1. Stupid DAO

  - Domain of user
  ~~~java
      public class User {
    	
    	String id;
    	String name;
    	String password;
    	
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
  
  - dao
  
  ~~~java
  public class UserDao {
	
  	public void add(User user)throws ClassNotFoundException, SQLException{
  		
  		Class.forName("oracle.jdbc.driver.OracleDriver");
  		
  		Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl1","HJEONG","1111");
  		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
  		ps.setString(1,user.getId());
  		ps.setString(2,user.getName());
  		ps.setString(3,user.getPassword());
  		
  		ps.executeUpdate();
  		
  		ps.close();
  		c.close();
  	}
  	
  	public User get(String id)throws ClassNotFoundException, SQLException{
  		
  		Class.forName("oracle.jdbc.driver.OracleDriver");
  		
  		Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl1","HJEONG","1111");
  		
  		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
  		ps.setString(1,id);
  		
  		ResultSet rs = ps.executeQuery();
  		rs.next();
  		User user = new User();
  		user.setId(rs.getString("id"));
  		user.setName(rs.getString("name"));
  		user.setPassword(rs.getString("password"));
  		
  		rs.close();
  		ps.close();
  		c.close();
  		
  		return user;
  		
  	}
  	
  	public static void main(String[]args)throws ClassNotFoundException, SQLException{
  		
  		UserDao dao = new UserDao();
  		
  		User user = new User();
  		user.setId("Kyle");
  		user.setName("Hee");
  		user.setPassword("1111");
  		
  		dao.add(user);
  		
  		User user2 = dao.get(user.getId());
  		
  		System.out.println(user2.getName());
  		
  	}
  }

  ~~~

<hr>
#2. Separation of DAO

####2_1 Separation of Concerns
	
* Separate each concerns to improve programming code
* If Concerns will be separated, We could be more focus on our concern

####2_2. Extract of Making connection
	
##### UserDao's concern

* How to get a __Connection__ to connect the DB
* How to make and execute a __Statement__ to apply a DB
* How to handle our __resources__(Statement, Connection)

##### Method extract of overlap code

  * To separate a overlap code that get a __Connection__
   
 ~~~java

	public void add(User user)throws ClassNotFoundException, SQLException{
		
		Connection c = getConnection();
		
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1,user.getId());
		ps.setString(2,user.getName());
		ps.setString(3,user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id)throws ClassNotFoundException, SQLException{
		
		
		Connection c = getConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1,id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
		
	}
	
	//To create a new method to get a connection
	private Connection getConnection() throws ClassNotFoundException, SQLException{        
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/test","root","1111");
		return c;
		
	}
~~~

###2_3. Independency of making DB Connection

####Extend through inheritance

 * abstract class UserDao
 ~~~java
 	public abstract class UserDao {
	
		public void add(User user)throws ClassNotFoundException, SQLException{
		
			Connection c = getConnection();
			
			PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
			ps.setString(1,user.getId());
			ps.setString(2,user.getName());
			ps.setString(3,user.getPassword());
			
			ps.executeUpdate();
			
			ps.close();
			c.close();
		}
	
		public User get(String id)throws ClassNotFoundException, SQLException{
			
			
			Connection c = getConnection();
			
			PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1,id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			
			rs.close();
			ps.close();
			c.close();
			
			return user;
			
		}
	// specifications of code was deleted and method change into abstract
	// submethod is in charge implementation method
		public abstract Connection getConnection() throws ClassNotFoundException, SQLException;  
	
	
		public static void main(String[]args)throws ClassNotFoundException, SQLException{
				
				UserDao dao = new NUserDao(); // It must be changed to use a Mysql
				
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

* NUserDao.class - Mysql

~~~java
	public class NUserDao extends UserDao{
		public Connection getConnection() throws ClassNotFoundException, SQLException{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/test","root","1111");
			return c;
		}
	}
~~~

* DUserDao.class - Oracle
~~~java
	public class DUserDao extends UserDao{
		
		public Connection getConnection() throws ClassNotFoundException, SQLException{
			Class.forName("oracle.jdbc.driver.OracleDriver");
	  		Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl1","HJEONG","1111");
	  		return c;
		}
	}
~~~

####**pros**
  *  We call it __Template method pattern__ or __Factory method pattern__
  *  With two concerns separated, work of change is easier 
  *  When we make a new DB Connection we don't care about UserDao 

####**cons**
  * We use a inheritance(extend)
  * Inheritance have a lot of restriction

<hr>

#3. Extend of DAO

####3_1. Separation of Class

	How to extend concerns easily while separating

* UserDao.class	
~~~java
	public class UserDao {
		
		private SimpleConnectionMaker simpleConnectionMaker;
		
		public UserDao(){
			simpleConnectionMaker = new SimpleConnectionMaker();
		}
		
		public void add(User user)throws ClassNotFoundException, SQLException{
			
			Connection c = simpleConnectionMaker.getConnection();
			
			PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
			ps.setString(1,user.getId());
			ps.setString(2,user.getName());
			ps.setString(3,user.getPassword());
			
			ps.executeUpdate();
			
			ps.close();
			c.close();
		}
		
		public User get(String id)throws ClassNotFoundException, SQLException{
			
			
			Connection c = simpleConnectionMaker.getConnection();
			
			PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1,id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			
			rs.close();
			ps.close();
			c.close();
			
			return user;
			
		}
		
		
		public static void main(String[]args)throws ClassNotFoundException, SQLException{
				
				UserDao dao = new UserDao();
				
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

* SimpleConnectionMaker.class

~~~java
	//We don't need a abstract class
	public class SimpleConnectionMaker {
	
		public Connection getConnection() throws ClassNotFoundException, SQLException{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/test","root","1111");
			return c;
		}
	}
~~~

####**pros**
 * The Code was improved

####**cons**
* We can't change DBConnection flexibly
* If user change __DBConnection method__ name, We must change our all method
* We have to know the __class that provide DBConnection__ 


####3_2 The introdunction of Interface

	We connect loosely(Interface) two classes to solve above the problem

* UserDao.class

~~~java
	public class UserDao {
		
		private ConnectionMaker connectionMaker; //We don't need to know specific Class name
		
		public UserDao(){
			connectionMaker = new NConnectionMaker(); //but, We have to fix the problem
		}
		
		public void add(User user)throws ClassNotFoundException, SQLException{
			
			Connection c = connectionMaker.makeConnection(); //If the class will be changed, We don't care 
			
			PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
			ps.setString(1,user.getId());
			ps.setString(2,user.getName());
			ps.setString(3,user.getPassword());
			
			ps.executeUpdate();
			
			ps.close();
			c.close();
		}
		
		public User get(String id)throws ClassNotFoundException, SQLException{
			
			
			Connection c = connectionMaker.makeConnection();
			
			PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1,id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			
			rs.close();
			ps.close();
			c.close();
			
			return user;
			
		}
		
		
		public static void main(String[]args)throws ClassNotFoundException, SQLException{
				
				UserDao dao = new UserDao();
				
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

* ConnectionMaker.class(Interface)

~~~java

	public interface ConnectionMaker {
		
		public Connection makeConnection() throws ClassNotFoundException, SQLException;
	}

~~~

* NConnectionMaker.class(implement) -> mysql

~~~java
	public class NConnectionMaker implements ConnectionMaker{
		public Connection makeConnection() throws ClassNotFoundException, SQLException{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/test","root","1111");
			return c;
		}
	}
~~~

* DConnectionMaker.class(implement) -> Oracle

~~~java

	public class DConnectionMaker implements ConnectionMaker{
		
		public Connection makeConnection() throws ClassNotFoundException, SQLException{
			Class.forName("oracle.jdbc.driver.OracleDriver");
	  		Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl1","HJEONG","1111");
	  		return c;
		}
	}
~~~

####**pros**
* We don't need to fix the UserDao, though N or D would change their a DBConnection

####**cons**
* We decide a object of class by using  constructor

####3_3. Separation of setting of relation
	
	The Code have a problem with concern of UserDao 
	The Concern How to connect UserDao and Implementation class 

* UserDao.class

~~~java
	public class UserDao {
		
		private ConnectionMaker connectionMaker; 
		
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
			
			
			Connection c = connectionMaker.makeConnection();
			
			PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1,id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			
			rs.close();
			ps.close();
			c.close();
			
			return user;
			
		}
	}
~~~

* UserDaoTest.class

~~~java
	public class UserDaoTest {
		public static void main(String[]args)throws ClassNotFoundException, SQLException{
			
			//This line decide to DBConnection for UserDao
			ConnectionMaker connectionMaker = new NConnectionMaker();
			
			//Provide Object of ConnectionMaker type with UserDao
			UserDao dao = new UserDao(connectionMaker);
			
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

####**pros**
* UserDao can focus on Own work
* If DBConnection will be changed, We will just fix one point 


####3_4. Principle and Pattern

* OCP(Open-Closed Principle) 
	- This is one of the design principle for OOP
	- Class or Module has to be opened about extension. on the contrary, it has to be closed about change
	- ex) UserDao is opened about DBConnection, UserDao is closed about implementation code 

* High coherence and Low coupling
	- High cohenrence
		- High cohenrence means some module will be changed since tiny change occur
		- UserDao.class is High cohenrence
	
	- Low coupling
		- If some change occur, it doesn't effect other modules  
		- It has a low coupling between Userdao and ConnectionMaker

* Strategy Pattern
	- Strategy Pattern is to best fit the OCP(Open-Closed Principle) 
	- UserDao.class == Context, How to connect DB(implementation class) == Strategy
<hr>

# 4. IOC(Inversion of Control)

#### 4_1. Object Factory

	UserDaoTest.class has a concern of connecting which implementation class(ConnectionMaker.class) 
	Actually, I made a UserDaoTest.class to test, so It should only be used in testing

* Factory

	- Factory is a object
	- Factory decide how to make a object and return a object

* DaoFactory.class

~~~java
	public class DaoFactory {
		
		public UserDao userDao(){ //Method of Factory will decide how to make type of UserDao object 
			ConnectionMaker connectionMaker = new NConnectionMaker();
			UserDao userDao = new UserDao(connectionMaker);
			return userDao;
		}
	
	}
~~~

* UserDaoTest.class

~~~java
	public class UserDaoTest {
		public static void main(String[]args)throws ClassNotFoundException, SQLException{
			
			
			UserDao dao = new DaoFactory().userDao();
			
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

* Factory as a blueprint
	
	Factory class(DaoFactory.class) is in charge of bluepring in this source
	As following the instruction,  Client(UserDaotest) just use UserDao 
	
#### 4_2. Using of Factory object

	If DaoFactory has a lot of DAO? 
	We should check each DAO to connect DBConnection

* FactoryDao.class
~~~java
	public class DaoFactory {
		
		public UserDao userDao(){
			return new UserDao(new NConnectionMaker()); //occurence of overlap
		}
		
		public AccountDao accountDao(){
			return new accountDao(new NConnectinoMaker());//occurence of overlap
		}
		
		public MessageDao messageDao(){
			return new messageDao(new NConnectinoMaker());//occurence of overlap
		}
	
	}
~~~

* Improving 

~~~java
	public class DaoFactory {
		
		public UserDao userDao(){
			return new UserDao(connectionMaker()); 
		}
		
		public AccountDao accountDao(){
			return new accountDao(connectionMaker());
		}
		
		public MessageDao messageDao(){
			return new messageDao(connectionMaker());
		}
		
		public ConnectionMaker connectionMaker(){
			return new NConnectionMaker();
		}
	
	}
~~~

#### 4_3. IOC through transfer of right to control

	IOC : Control flow of program is changed 

* Object in IOC don't choose using object and don't create object 
* Object in IOC don't know where they come form 
* All right to control delegate other object, not own
* Framework must have a IOC 



