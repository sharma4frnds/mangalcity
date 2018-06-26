package net.clamour.mangalcity.ResponseModal;

import java.util.List;

/**
 * Created by clamour_5 on 6/20/2018.
 */

public class FeedsResponse {
    public Boolean success;

     List<CountryPostResponse> country_posts;
     List<CountryPostResponse> district_posts;
     List<CountryPostResponse>state_posts;
     List<CountryPostResponse>city_posts;


    public List<CountryPostResponse> getCity_posts() {
        return city_posts;
    }

    public List<CountryPostResponse> getState_posts() {
        return state_posts;
    }

    public List<CountryPostResponse> getDistrict_posts() {
        return district_posts;
    }

    public List<CountryPostResponse> getCountry_posts() {
        return country_posts;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
