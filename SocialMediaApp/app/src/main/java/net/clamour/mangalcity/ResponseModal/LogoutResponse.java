package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 6/20/2018.
 */

public class LogoutResponse {
    public Boolean success;
    public String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
