package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 7/3/2018.
 */

public class DistrictMainResponse {

    public Boolean success;


    public District_Posts district_posts;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public District_Posts getDistrict_posts() {
        return district_posts;
    }

    public void setDistrict_posts(District_Posts district_posts) {
        this.district_posts = district_posts;
    }
}
