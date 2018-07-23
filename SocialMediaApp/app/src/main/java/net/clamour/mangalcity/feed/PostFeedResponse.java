
package net.clamour.mangalcity.feed;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.clamour.mangalcity.ResponseModal.Country_Posts;
import net.clamour.mangalcity.ResponseModal.District_Posts;
import net.clamour.mangalcity.ResponseModal.State_Posts;

public class PostFeedResponse {

    @SerializedName("city_posts")
    private CityPosts cityPosts;
    @Expose
    private Boolean success;

    @SerializedName("country_posts")
    private Country_Posts country_posts;

    @SerializedName("state_posts")
    private State_Posts state_posts;

    @SerializedName("district_posts")
    private District_Posts district_posts;

    public CityPosts getCityPosts() {
        return cityPosts;
    }

    public State_Posts getState_posts() {
        return state_posts;
    }

    public void setState_posts(State_Posts state_posts) {
        this.state_posts = state_posts;
    }

    public District_Posts getDistrict_posts() {
        return district_posts;
    }

    public void setDistrict_posts(District_Posts district_posts) {
        this.district_posts = district_posts;
    }

    public Country_Posts getCountry_posts() {
        return country_posts;
    }

    public void setCountry_posts(Country_Posts country_posts) {
        this.country_posts = country_posts;
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
