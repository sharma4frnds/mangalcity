
package net.clamour.mangalcity.ResponseModal;


import com.google.gson.annotations.SerializedName;


public class Pdata {

    @SerializedName("created_at")
    private CreatedAt mCreatedAt;
    @SerializedName("dislikes")
    private String mDislikes;
    @SerializedName("id")
    private Long mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("likes")
    private String mLikes;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("name")
    private String mName;
    @SerializedName("tag")
    private Long mTag;
    @SerializedName("type")
    private String mType;
    @SerializedName("user_id")
    private Long mUserId;
    @SerializedName("value")
    private String mValue;

    public CreatedAt getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(CreatedAt createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDislikes() {
        return mDislikes;
    }

    public void setDislikes(String dislikes) {
        mDislikes = dislikes;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getLikes() {
        return mLikes;
    }

    public void setLikes(String likes) {
        mLikes = likes;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getTag() {
        return mTag;
    }

    public void setTag(Long tag) {
        mTag = tag;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
