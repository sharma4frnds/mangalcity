package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 6/22/2018.
 */

public class LikeResponse {

    Boolean success;
    String lcount;
    String dcount;

    public String getLcount() {
        return lcount;
    }

    public void setLcount(String lcount) {
        this.lcount = lcount;
    }

    public String getDcount() {
        return dcount;
    }

    public void setDcount(String dcount) {
        this.dcount = dcount;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
