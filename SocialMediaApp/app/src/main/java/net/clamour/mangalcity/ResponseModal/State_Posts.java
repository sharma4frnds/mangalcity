package net.clamour.mangalcity.ResponseModal;

import java.util.List;

/**
 * Created by clamour_5 on 7/2/2018.
 */

public class State_Posts {

    private String last_page;

    List<CityPostResponse> data;


    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public List<CityPostResponse> getData() {
        return data;
    }

    public void setData(List<CityPostResponse> data) {
        this.data = data;
    }


}
