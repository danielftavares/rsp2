package com.procergs.rsp.post.ed;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

import com.procergs.rsp.user.ed.UserEd;

@Entity
@Table(name = "USER_LIKE")
public class LikeED implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private UserEd userEd;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_POST")
	@XmlTransient
	private PostED postED;
	
	@Column(name = "DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	public UserEd getUserEd() {
		return userEd;
	}

	public void setUserEd(UserEd userEd) {
		this.userEd = userEd;
	}

	@XmlTransient
	public PostED getPostED() {
		return postED;
	}

	public void setPostED(PostED postED) {
		this.postED = postED;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
}
