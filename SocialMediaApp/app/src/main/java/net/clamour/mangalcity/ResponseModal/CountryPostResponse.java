package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 6/20/2018.
 */

public class CountryPostResponse {

    private String id;
    private String user_id;
    private String message;
    private String type;
    private String value;
    private String likes;
    private String dislikes;
    private String created_at;
//    private String like;
////
//
//    public String getLike() {
//        return like;
//    }
//
//    public void setLike(String like) {
//        this.like = like;
//    }

    public UserPostResponse user;
    public OwnLikeResponse like;

    public UserPostResponse getUser() {
        return user;
    }

    public void setUser(UserPostResponse user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

//    public OwnLikeResponse getLike() {
//        return like;
//    }
//
//    public void setLike(OwnLikeResponse like) {
//        this.like = like;
//    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

//        public String getLike() {
//        return like;
//    }
//
//    public void setLike(String like) {
//        this.like = like;
//    }

}
