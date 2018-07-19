package net.clamour.mangalcity.ResponseModal;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Like {

    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private String id;
    @SerializedName("post_id")
    private String postId;
    @Expose
    private String type;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private Long userId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Like{" +
                "createdAt='" + createdAt + '\'' +
                ", id=" + id +
                ", postId=" + postId +
                ", type=" + type +
                ", updatedAt='" + updatedAt + '\'' +
                ", userId=" + userId +
                '}';
    }
}
