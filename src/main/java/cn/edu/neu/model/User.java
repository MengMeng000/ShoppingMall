package cn.edu.neu.model;

public class User {

	private int userId;
	private int userAge;
	private String userName;
	private String userPass;
	private int userSex;
	private String userEmail;
	private int userRank;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserAge() {
		return userAge;
	}
	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public int getUserSex() {
		return userSex;
	}
	public void setUserSex(int userSex) {
		this.userSex = userSex;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public int getUserRank() {
		return userRank;
	}
	public void setUserRank(int userRank) {
		this.userRank = userRank;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userAge=" + userAge + ", userNamel=" + userName + ", userPass=" + userPass
				+ ", userSex=" + userSex + ", userEmail=" + userEmail + ", userRank=" + userRank + ", getUserId()="
				+ getUserId() + ", getUserAge()=" + getUserAge() + ", getUserNamel()=" + getUserName()
				+ ", getUserPass()=" + getUserPass() + ", getUserSex()=" + getUserSex() + ", getUserEmail()="
				+ getUserEmail() + ", getUserRank()=" + getUserRank() + "]";
	}
	
	
	
	
}
