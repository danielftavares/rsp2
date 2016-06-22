package com.procergs.rsp.user.ed;

import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import com.procergs.rsp.image.ed.ImageED;
import com.procergs.rsp.profile.ed.ProfileFieldValue;

@Entity
@Table(name = "RSP_USUARIO")
public class UserEd {

	@Id
	@Column(name = "ID_USUARIO")
	@SequenceGenerator(name = "ID_RSP_USUARIO_SEQ", sequenceName = "ID_RSP_USUARIO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_RSP_USUARIO_SEQ")
	private Long idUsuario;
	
	
	@Column(name = "LOGIN")
	private String login;
	
	@Column(name = "NOME")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name="ID_PROFILE_IMAGE", referencedColumnName = "ID_IMAGE")
	private ImageED profileImage;

	@Column(name="LDAP")
	@XmlTransient
	private Boolean ldap;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ImageED getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(ImageED profileImage) {
		this.profileImage = profileImage;
	}

	@XmlTransient	
	public Boolean getLdap() {
		return ldap;
	}

	public void setLdap(Boolean ldap) {
		this.ldap = ldap;
	}
	
	

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEd other = (UserEd) obj;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		return true;
	}
	
}
