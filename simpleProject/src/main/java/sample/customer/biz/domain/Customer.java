package sample.customer.biz.domain;

public class Customer implements java.io.Serializable {
	
	private int id;
	private String name;
	private String address;
	private String emailAddress;
	
	public Customer(String name, String address, String emailAddress){
		this.name=name;
		this.address=address;
		this.emailAddress = emailAddress;
	}
	
	public boolean isNgEmail(){
		return emailAddress.matches(".*ng.foo.baz$");
	}
	
	public boolean isFreeEmail(){
		return emailAddress.matches(".*free.foo.baz$");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
}
