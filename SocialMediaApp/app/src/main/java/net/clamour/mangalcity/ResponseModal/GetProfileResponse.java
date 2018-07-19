package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 6/28/2018.
 */

public class GetProfileResponse {

    Boolean success;

    public UserProfileResponse user;

    String current_location;

String home_location;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UserProfileResponse getUser() {
        return user;
    }

    public void setUser(UserProfileResponse user) {
        this.user = user;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    public String getHome_location() {
        return home_location;
    }

    public void setHome_location(String home_location) {
        this.home_location = home_location;
    }
}
