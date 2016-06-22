package com.procergs.rsp.post.ed;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 09/03/16.
 */
public class PostSearchResultED implements Serializable {

    private Set<PostResultED> posts;


    public Set<PostResultED> getPosts() {
        return posts;
    }

    public void setPosts(Set<PostResultED> posts) {
        this.posts = posts;
    }
}
