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

1) Separation of Concerns
	
* Separate each concerns to improve programming code
* If Concerns will be separated, We could be more focus on our concern


2). Extract of Making connection
	
(1) UserDao's concern

* How to get a __Connection__ to connect the DB
* How to make and execute a __Statement__ to apply a DB
* How to handle our __resources__(Statement, Connection)

(2) Method extract of overlap code

* To do
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

  
