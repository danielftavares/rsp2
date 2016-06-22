package com.procergs.rsp.profile.ed;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.procergs.rsp.user.ed.UserEd;


@Entity
@Table(name = "PROFILE_FIELD_VALUE")
public class ProfileFieldValue implements Serializable {
	
	@Id
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private UserEd profile;
	
	@Id
	@ManyToOne
	@JoinColumn(name="ID_PROFILE_FIELD")
	private ProfileField profileField;
	
	@Column(name="value")
	private String value;

	public UserEd getProfile() {
		return profile;
	}

	public void setProfile(UserEd profile) {
		this.profile = profile;
	}

	public ProfileField getProfileField() {
		return profileField;
	}

	public void setProfileField(ProfileField profileField) {
		this.profileField = profileField;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
