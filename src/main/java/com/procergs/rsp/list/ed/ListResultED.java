package com.procergs.rsp.list.ed;

import com.procergs.rsp.user.ed.UserEd;

import java.util.List;

/**
 * Created by daniel on 27/06/16.
 */
public class ListResultED {

    private Long idList;
    private String name;
    private List<UserEd> followers;

    public Long getIdList() {
        return idList;
    }

    public void setIdList(Long idList) {
        this.idList = idList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEd> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserEd> followers) {
        this.followers = followers;
    }
}
