package com.procergs.rsp.user.ed;

import java.util.List;

public class FollowingFollowersED {
	
	private List<UserEd> following;
	
	private List<UserEd> followers;

	public List<UserEd> getFollowing() {
		return following;
	}

	public void setFollowing(List<UserEd> following) {
		this.following = following;
	}

	public List<UserEd> getFollowers() {
		return followers;
	}

	public void setFollowers(List<UserEd> followers) {
		this.followers = followers;
	}

	
}
