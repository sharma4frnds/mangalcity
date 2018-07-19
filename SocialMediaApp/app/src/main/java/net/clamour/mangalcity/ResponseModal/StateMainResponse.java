package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 7/3/2018.
 */

public class StateMainResponse {

    public Boolean success;

    public State_Posts state_posts;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public State_Posts getState_posts() {
        return state_posts;
    }

    public void setState_posts(State_Posts state_posts) {
        this.state_posts = state_posts;
    }
}
