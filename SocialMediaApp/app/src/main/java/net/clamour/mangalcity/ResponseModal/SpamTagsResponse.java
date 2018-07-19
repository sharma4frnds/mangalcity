package net.clamour.mangalcity.ResponseModal;

import java.util.List;

/**
 * Created by clamour_5 on 6/28/2018.
 */

public class SpamTagsResponse {
    Boolean success;
    List<SpamDataResponse> data;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<SpamDataResponse> getData() {
        return data;
    }

    public void setData(List<SpamDataResponse> data) {
        this.data = data;
    }
}
