package net.clamour.mangalcity.Home;

/**
 * Created by clamour_5 on 7/19/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.R;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class SearchAdapter extends ArrayAdapter<String> {

    JSONArray search_array;

    Activity context;
    public SearchAdapter(Context context, int textViewResourceId,
                          JSONArray arr) {
        super(context, textViewResourceId);
        search_array=arr;
        this.context=(Activity) context;
        this.color=color;




    }

    String name = "";
   String city="";
   String image="";
    int color;




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_search,null,true);
        TextView name_text=(TextView)rowView.findViewById(R.id.user_name);
        TextView city_text=(TextView)rowView.findViewById(R.id.user_city);
        ImageView user_iamge=(ImageView)rowView.findViewById(R.id.user_image);


        try {
            JSONObject object = search_array.getJSONObject(position);


                name = object.getString("name");
                city=object.getString("city");
                image=object.getString("image");
            Log.d(TAG, "getView: "+image);

            name_text.setText(name);
            city_text.setText(city);

            Glide.with(context).load(image)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(user_iamge);





        }
 catch (Exception e)
            {
                Log.i("Exception",""+e);
            }















//            String hex1=color.get(position);
//
//            arr.setTextColor(Color.parseColor(hex1));
//            service.setTextColor(Color.parseColor(hex1));
//            catlog.setTextColor(Color.parseColor("#d3d3d3"));




        // service.setBackgroundColor(Color.parseColor(color));






//              AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, 100);
//        rowView.setLayoutParams(params);


        return rowView;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return search_array.length();
    }

}
