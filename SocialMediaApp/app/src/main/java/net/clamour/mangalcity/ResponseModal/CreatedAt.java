
package net.clamour.mangalcity.ResponseModal;


import com.google.gson.annotations.SerializedName;


public class CreatedAt {

    @SerializedName("date")
    private String mDate;
    @SerializedName("timezone")
    private String mTimezone;
    @SerializedName("timezone_type")
    private Long mTimezoneType;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public Long getTimezoneType() {
        return mTimezoneType;
    }

    public void setTimezoneType(Long timezoneType) {
        mTimezoneType = timezoneType;
    }

}
