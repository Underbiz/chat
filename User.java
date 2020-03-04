package chat.network;

public class User {
	private String mail;
	private String nickName;
	private int age;
	
	
	public User(String nickName, String mail, int age) {
		
		this.nickName = nickName;
		this.mail = mail;
		this.age = age;
		
	}
	public String getName() {
		return nickName;
	}
	public void setName(String name) {
		this.nickName = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	

}
