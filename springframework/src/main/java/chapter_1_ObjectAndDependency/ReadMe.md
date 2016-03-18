#ObjectAndDependency

<hr>

##1. Stupid DAO

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
##2. Separation of DAO

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

##3. Extend of DAO

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


