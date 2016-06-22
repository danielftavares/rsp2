package com.procergs.rsp.post.ed;

import com.procergs.rsp.image.ed.ImageED;
import com.procergs.rsp.list.ed.ListED;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by daniel on 13/03/16.
 */
public class PostResultEDBuilder {
    PostED postED;
    Collection<LikeED> likes;
    Collection<ImageED> images;
    Collection<PostResultED> replies;




    public PostResultEDBuilder(PostED postED, Stream<PostResultED> postResultEDStream, Collection<ImageED> imageEDs, Collection<LikeED> likeEDs) {
        this.postED = postED;
        this.likes = likeEDs;
        this.images = imageEDs;
        this.replies = postResultEDStream.collect(Collectors.toList());
    }

    public PostResultED build(){
        if(postED == null){
            return null;
        }
        PostResultED resultED = new PostResultED();
        resultED.setIdPost(postED.getIdPost());
        resultED.setData (postED.getData());
        resultED.setTexto (postED.getTexto());
        resultED.setIdUser(postED.getUserEd().getIdUsuario());
        resultED.setName(postED.getUserEd().getNome());
        resultED.setReplies(replies);
        if(postED.getUserEd().getProfileImage() != null){
            resultED.setIdProfileImage(postED.getUserEd().getProfileImage().getIdImage());
            resultED.setProfileImageType(postED.getUserEd().getProfileImage().getType());
        }

        if(postED.getLists() != null && !postED.getLists().isEmpty()){
            resultED.setLists(postED.getLists().stream().map(listED -> buildListResultED(listED)).collect(Collectors.toList()));
        }

        resultED.setLikes(likes.stream().map(likeED -> buildLikeResultED(likeED)).collect(Collectors.toList()));
        resultED.setImages (images.stream().map(imageED -> buildImageResultED(imageED)).collect(Collectors.toList()));

        resultED.setOpenGraphED(postED.getOpenGraphED());
        return resultED;
    }

    private ImageResultED buildImageResultED(ImageED imageED) {
        ImageResultED imageResultED = new ImageResultED();
        imageResultED.setIdImage(imageED.getIdImage());
        imageResultED.setType(imageED.getType());
        return imageResultED;
    }

    private LikeResultED buildLikeResultED(LikeED likeED) {
        LikeResultED likeResultED = new LikeResultED();
        likeResultED.setIdUser(likeED.getUserEd().getIdUsuario());
        likeResultED.setName(likeED.getUserEd().getNome());
        if(likeED.getUserEd().getProfileImage() != null){
            likeResultED.setIdProfileImage(likeED.getUserEd().getProfileImage().getIdImage());
            likeResultED.setProfileImageType(likeED.getUserEd().getProfileImage().getType());
        }
        return likeResultED;
    }

    private ListResultED buildListResultED(ListED listED){
        ListResultED listResultED = new ListResultED();
        listResultED.setName(listED.getName());
        listResultED.setIdList(listED.getIdList());
        return listResultED;
    }
}
