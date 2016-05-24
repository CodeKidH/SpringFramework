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
	
	String id;
	String name;
	String password;
	
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
    
    On the inside It has a int, but on the outside It has a object so You can use it safely




  
  
