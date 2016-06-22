package com.procergs.rsp.user.ed;

public class UserRequestED {
	public static String ATRIBUTO_REQ_USER         = "RSPUSER";
	
	private UserEd userEd;

	public UserRequestED(UserEd userEd) {
		this.userEd = userEd;
	}

	public UserEd getUserEd() {
		return userEd;
	}

	public void setUserEd(UserEd userEd) {
		this.userEd = userEd;
	}
	
}
