package net.clamour.mangalcity.PostTabs;


public class PostModalClass {
    private String user_name;
    private String post_timing;
    private String post_text;
    private int post_image;
    private int like_image;
    private int dislike_image;
    private int comment_image;
    private int share_image;
    private int user_image;

    public PostModalClass(String user_name, String post_timing, String post_text, int post_image, int like_image, int dislike_image, int comment_image, int share_image,int user_image) {
        this.user_name = user_name;
        this.post_timing = post_timing;
        this.post_text = post_text;
        this.post_image = post_image;
        this.like_image = like_image;
        this.dislike_image = dislike_image;
        this.comment_image = comment_image;
        this.share_image = share_image;
        this.user_image=user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPost_timing() {
        return post_timing;
    }

    public void setPost_timing(String post_timing) {
        this.post_timing = post_timing;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public int getPost_image() {
        return post_image;
    }

    public void setPost_image(int post_image) {
        this.post_image = post_image;
    }

    public int getLike_image() {
        return like_image;
    }

    public void setLike_image(int like_image) {
        this.like_image = like_image;
    }

    public int getDislike_image() {
        return dislike_image;
    }

    public void setDislike_image(int dislike_image) {
        this.dislike_image = dislike_image;
    }

    public int getComment_image() {
        return comment_image;
    }

    public void setComment_image(int comment_image) {
        this.comment_image = comment_image;
    }

    public int getShare_image() {
        return share_image;
    }

    public void setShare_image(int share_image) {
        this.share_image = share_image;
    }

    public int getUser_image() {
        return user_image;
    }

    public void setUser_image(int user_image) {
        this.user_image = user_image;
    }
}
