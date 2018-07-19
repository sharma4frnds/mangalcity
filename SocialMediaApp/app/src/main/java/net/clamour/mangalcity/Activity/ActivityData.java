package net.clamour.mangalcity.Activity;

/**
 * Created by clamour_5 on 7/19/2018.
 */

public class ActivityData {

    public ActivityPost post;


    private String id;
    private String user_id;
    private String type;
    private String post_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public ActivityPost getPost() {
        return post;
    }

    public void setPost(ActivityPost post) {
        this.post = post;
    }
}
