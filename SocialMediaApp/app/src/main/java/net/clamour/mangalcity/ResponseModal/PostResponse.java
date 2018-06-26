package net.clamour.mangalcity.ResponseModal;

/**
 * Created by clamour_5 on 6/18/2018.
 */

import com.google.gson.annotations.SerializedName;


public class PostResponse {

    @SerializedName("pdata")
    private  Pdata mPdata;
    @SerializedName("success")
    private Boolean mSuccess;

    public Pdata getPdata() {
        return mPdata;
    }

    public void setPdata(Pdata pdata) {
        mPdata = pdata;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }


    @Override
    public String toString() {
        return "UploadResponse{" +
                "mPdata=" + mPdata +
                ", mSuccess=" + mSuccess +
                '}';
    }
}
