package com.procergs.rsp.user.ed;

import javax.persistence.*;

import com.procergs.rsp.list.ed.ListED;


@Entity
@Table(name = "RSP_FOLLOW")
public class FollowED {
	
	@Id
	@SequenceGenerator(name = "ID_FOLLOW_SEQ", sequenceName = "ID_FOLLOW_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_FOLLOW_SEQ")
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
