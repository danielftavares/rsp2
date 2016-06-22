package com.procergs.rsp.post.ed;
import com.procergs.rsp.opengraph.ed.OpenGraphED;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by daniel on 13/03/16.
 */
public class PostResultED {

    private Long idPost;

    private String texto;

    private Calendar data;

    private Long idUser;

    private String name;

    private Long idProfileImage;

    private String profileImageType;

    private List<LikeResultED> likes;

    private List<ImageResultED> images;

    private Collection<PostResultED> replies;

    private Collection<ListResultED> lists;

    private OpenGraphED openGraphED;

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public List<LikeResultED> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeResultED> likes) {
        this.likes = likes;
    }

    public List<ImageResultED> getImages() {
        return images;
    }

    public void setImages(List<ImageResultED> images) {
        this.images = images;
    }

    public Collection<PostResultED> getReplies() {
        return replies;
    }

    public void setReplies(Collection<PostResultED> replies) {
        this.replies = replies;
    }

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

    public Collection<ListResultED> getLists() {
        return lists;
    }

    public void setLists(Collection<ListResultED> lists) {
        this.lists = lists;
    }

    public OpenGraphED getOpenGraphED() {
        return openGraphED;
    }

    public void setOpenGraphED(OpenGraphED openGraphED) {
        this.openGraphED = openGraphED;
    }
}