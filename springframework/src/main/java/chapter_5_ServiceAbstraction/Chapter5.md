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



  
  
