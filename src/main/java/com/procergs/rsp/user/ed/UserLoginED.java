package com.procergs.rsp.user.ed;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "usuario_login")
public class UserLoginED implements Serializable{
	
	@Id
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private UserEd userEd;
	
	@Column(name = "TOKEN")
	private String token;

	@Id
	@Column(name = "DATA_LOGIN")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataLogin;
	
	@Column(name = "DATA_LOGOUT")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataLogout;
	

	public UserEd getUserEd() {
		return userEd;
	}

	public void setUserEd(UserEd userEd) {
		this.userEd = userEd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Calendar getDataLogin() {
		return dataLogin;
	}

	public void setDataLogin(Calendar dataLogin) {
		this.dataLogin = dataLogin;
	}

	public Calendar getDataLogout() {
		return dataLogout;
	}

	public void setDataLogout(Calendar dataLogout) {
		this.dataLogout = dataLogout;
	}

	
}
