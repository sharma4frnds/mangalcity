package net.clamour.mangalcity.Home;

/**
 * Created by clamour_5 on 5/14/2018.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.clamour.mangalcity.R;

import static android.content.Context.MODE_PRIVATE;


public class CustomAdapterDrawer extends ArrayAdapter<String> {

    String web[];
    Integer img[];
    Activity context;


    SharedPreferences login_prefrence;
    SharedPreferences Registration_preferences;
    String UserName;

    String auth_id,first_name,last_name,complete_name,email;
    public CustomAdapterDrawer(Activity context, String[] web,
                               Integer[] img) {
        super(context, android.R.layout.simple_list_item_1, web);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.img=img;
        this.web=web;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.draweritems,null,true);
//        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, 150);
//        rowView.setLayoutParams(params);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.compalint_row_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.complaint_row_img);



        txtTitle.setText(web[position]);
        imageView.setImageResource(img[position]);

       // Registration_preferences = context.getSharedPreferences("net.clamour.detlef.Profile.RegistrationScreen", MODE_PRIVATE);
        //UserName = Registration_preferences.getString("UserName", "");


        if(position==0){
            rowView=inflater.inflate(R.layout.profile_row,null,true);
//            AbsListView.LayoutParams params1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, 200);
//            rowView.setLayoutParams(params1);
//            TextView name=(TextView)rowView.findViewById(R.id.name);
//            name.setText("Hi"+" "+UserName);
        }
//        if(position==1){
//            rowView=inflater.inflate(R.layout.host_swap,null,true);
//
//
//
//        }











        return rowView;
    }

}
