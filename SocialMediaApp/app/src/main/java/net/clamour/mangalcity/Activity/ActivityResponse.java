package net.clamour.mangalcity.Activity;

import android.app.Activity;

/**
 * Created by clamour_5 on 7/19/2018.
 */

public class ActivityResponse {

 public ActivityResponseData activity;

    private Boolean success;

    public ActivityResponseData getActivity() {
        return activity;
    }

    public void setActivity(ActivityResponseData activity) {
        this.activity = activity;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
