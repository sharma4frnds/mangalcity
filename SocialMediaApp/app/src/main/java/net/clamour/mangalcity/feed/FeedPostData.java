
package net.clamour.mangalcity.feed;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class FeedPostData {

    @Expose
    private Long city;
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private Long dislikes;
    @Expose
    private Long district;
    @Expose
    private Long id;
    @Expose
    private Like like;
    @Expose
    private Long likes;
    @Expose
    private String message;
    @Expose
    private Long share;
    @Expose
    private Long spam;
    @Expose
    private Long state;
    @Expose
    private Long status;
    @Expose
    private String tag;
    @Expose
    private String type;
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    private User user;
    @SerializedName("user_id")
    private Long userId;

    public List<CommentShowData>comment;

    public List<CommentShowData> getComment() {
        return comment;
    }

    public List<MediaImageResponse>media;

    public List<MediaImageResponse> getMedia() {
        return media;
    }

    public void setMedia(List<MediaImageResponse> media) {
        this.media = media;
    }

    public void setComment(List<CommentShowData> comment) {
        this.comment = comment;
    }

    @Expose
    private String value;



    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getDislikes() {
        return dislikes;
    }

    public void setDislikes(Long dislikes) {
        this.dislikes = dislikes;
    }

    public Long getDistrict() {
        return district;
    }

    public void setDistrict(Long district) {
        this.district = district;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getShare() {
        return share;
    }

    public void setShare(Long share) {
        this.share = share;
    }

    public Long getSpam() {
        return spam;
    }

    public void setSpam(Long spam) {
        this.spam = spam;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FeedPostData{" +
                "city=" + city +
                ", createdAt='" + createdAt + '\'' +
                ", dislikes=" + dislikes +
                ", district=" + district +
                ", id=" + id +
                ", like=" + like +
                ", likes=" + likes +
                ", message='" + message + '\'' +
                ", share=" + share +
                ", spam=" + spam +
                ", state=" + state +
                ", status=" + status +
                ", tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                ", userId=" + userId +
                ", value='" + value + '\'' +
                '}';
    }
}
