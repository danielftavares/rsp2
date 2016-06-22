package com.procergs.rsp.post.ed;

import javax.persistence.Column;

/**
 * Created by daniel on 13/03/16.
 */
public class LikeResultED {

    private Long idUser;

    private String name;

    private Long idProfileImage;

    private String profileImageType;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdProfileImage() {
        return idProfileImage;
    }

    public void setIdProfileImage(Long idProfileImage) {
        this.idProfileImage = idProfileImage;
    }

    public String getProfileImageType() {
        return profileImageType;
    }

    public void setProfileImageType(String profileImageType) {
        this.profileImageType = profileImageType;
    }
}
