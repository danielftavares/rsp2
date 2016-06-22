package com.procergs.rsp.post.ed;

import javax.persistence.Column;

/**
 * Created by daniel on 13/03/16.
 */
public class ListResultED {

    private Long idList;

    private String name;

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
}
