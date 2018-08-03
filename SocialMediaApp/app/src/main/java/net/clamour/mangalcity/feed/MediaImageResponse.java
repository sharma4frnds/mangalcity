package net.clamour.mangalcity.feed;

/**
 * Created by clamour_5 on 8/2/2018.
 */

public class MediaImageResponse {
    private String id;
    private String post_id;
    private String name;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }
}
