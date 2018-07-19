package net.clamour.mangalcity.ResponseModal;


import java.util.List;

/**
 * Created by clamour_5 on 6/20/2018.
 */

public class FeedsResponse {
    public Boolean success;

    public City_Posts city_posts;
   // public Country_Posts country_posts;
    //public District_Posts district_posts;
    //public State_Posts state_posts;

    public City_Posts getCity_posts() {
        return city_posts;
    }

    public void setCity_posts(City_Posts city_posts) {
        this.city_posts = city_posts;
    }

//    public Country_Posts getCountry_posts() {
//        return country_posts;
//    }
//
//    public void setCountry_posts(Country_Posts country_posts) {
//        this.country_posts = country_posts;
//    }
//
//    public District_Posts getDistrict_posts() {
//        return district_posts;
//    }
//
//    public void setDistrict_posts(District_Posts district_posts) {
//        this.district_posts = district_posts;
//    }
//
//    public State_Posts getState_posts() {
//        return state_posts;
//    }
//
//    public void setState_posts(State_Posts state_posts) {
//        this.state_posts = state_posts;
//    }


    //     List<CountryPostResponse> country_posts;
//     List<CountryPostResponse> district_posts;
//     List<CountryPostResponse>state_posts;
//     List<CountryPostResponse>city_posts;


//    public List<CountryPostResponse> getCity_posts() {
//        return city_posts;
//    }
//
//    public List<CountryPostResponse> getState_posts() {
//        return state_posts;
//    }
//
//    public List<CountryPostResponse> getDistrict_posts() {
//        return district_posts;
//    }
//
//    public List<CountryPostResponse> getCountry_posts() {
//        return country_posts;
//    }
//

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
