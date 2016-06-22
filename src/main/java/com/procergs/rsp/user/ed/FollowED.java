package com.procergs.rsp.user.ed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.procergs.rsp.list.ed.ListED;


@Entity
@Table(name = "FOLLOW")
public class FollowED {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID_FOLLOW")
	private Long idFollow;
	
	@ManyToOne
	@JoinColumn(name="ID_FOLLOWER", referencedColumnName = "ID_USUARIO")
	private UserEd follower;
	
	@ManyToOne
	@JoinColumn(name="ID_FOLLOWED", referencedColumnName = "ID_USUARIO")
	private UserEd followed;
	
	
	@ManyToOne
	@JoinColumn(name="ID_LIST_FOLLOWED", referencedColumnName = "ID_LIST")
	private ListED listFollowed;

	public FollowED(UserEd follower, ListED listED) {
		this.follower = follower;
		this.listFollowed = listED;
	}

	public FollowED(UserEd follower, UserEd followed) {
		this.follower = follower;
		this.followed = followed;
	}
	
	public FollowED(){
		
	}

	public Long getIdFollow() {
		return idFollow;
	}

	public void setIdFollow(Long idFollow) {
		this.idFollow = idFollow;
	}

	public UserEd getFollower() {
		return follower;
	}

	public void setFollower(UserEd follower) {
		this.follower = follower;
	}

	public UserEd getFollowed() {
		return followed;
	}

	public void setFollowed(UserEd followed) {
		this.followed = followed;
	}

	public ListED getListFollowed() {
		return listFollowed;
	}

	public void setListFollowed(ListED listFollowed) {
		this.listFollowed = listFollowed;
	}
}
