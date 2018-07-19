package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 7/3/2018.
 */

public class CountryMainResponse {


    public Boolean success;

     public Country_Posts country_posts;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Country_Posts getCountry_posts() {
        return country_posts;
    }

    public void setCountry_posts(Country_Posts country_posts) {
        this.country_posts = country_posts;
    }
}
