# Exceptioin

## 1. Exception disappear

    
    * UserDao.class
    ~~~java
    //Before
    public void deleteAll()throws SQLException{
		
  		this.jdbcContext.executeSql("delete from users");
  		
  	}
  	
  	//After
	  public void deleteAll(){
		
  		this.jdbcTemplate.update("delete from users");
  		
  	}
    ~~~
      - Where is a Exception?
      
#### 1_1. Stupid Exception

* Exception Blackhole

    - Stupid 1
    ~~~java
    try{

    }catch(SQLException e){
    
    }
    
    // We don't have to do that
    // This exception will pass a error when Exception occur
    ~~~
    
    - Stupid 2
    

