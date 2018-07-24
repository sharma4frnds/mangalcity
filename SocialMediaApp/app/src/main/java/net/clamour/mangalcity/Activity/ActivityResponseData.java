package net.clamour.mangalcity.Activity;

import android.app.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.clamour.mangalcity.feed.FeedPostData;

import java.util.List;

/**
 * Created by clamour_5 on 7/19/2018.
 */

public class ActivityResponseData {

    private List<ActivityData>data;

    @SerializedName("current_page")
    private Long currentPage;

    @Expose
    private Long from;
    @SerializedName("last_page")
    private Long lastPage;
    @SerializedName("next_page_url")
    private String nextPageUrl;
    @Expose
    private String path;
    @SerializedName("per_page")
    private Long perPage;
    @SerializedName("prev_page_url")
    private Object prevPageUrl;
    @Expose
    private Long to;
    @Expose
    private Long total;

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }


    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getLastPage() {
        return lastPage;
    }

    public void setLastPage(Long lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getPerPage() {
        return perPage;
    }

    public void setPerPage(Long perPage) {
        this.perPage = perPage;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(Object prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }





    public List<ActivityData> getData() {
        return data;
    }

    public void setData(List<ActivityData> data) {
        this.data = data;
    }
}
