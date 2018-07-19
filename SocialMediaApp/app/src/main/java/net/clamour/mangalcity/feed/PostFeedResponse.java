
package net.clamour.mangalcity.feed;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostFeedResponse {

    @SerializedName("city_posts")
    private CityPosts cityPosts;
    @Expose
    private Boolean success;

    public CityPosts getCityPosts() {
        return cityPosts;
    }

    public void setCityPosts(CityPosts cityPosts) {
        this.cityPosts = cityPosts;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "PostFeedResponse{" +
                "cityPosts=" + cityPosts +
                ", success=" + success +
                '}';
    }
}
