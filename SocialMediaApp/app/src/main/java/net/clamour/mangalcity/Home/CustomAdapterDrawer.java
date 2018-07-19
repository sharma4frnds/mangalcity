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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.R;

import static android.content.Context.MODE_PRIVATE;


public class CustomAdapterDrawer extends ArrayAdapter<String> {

    String web[];
    Integer img[];
    Activity context;


    SharedPreferences LoginPrefrences;
    SharedPreferences Registration_preferences;
    String UserToken,profile_image,first_nameuser,last_nameuser;

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
        LoginPrefrences = context.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken=LoginPrefrences.getString("userToken","");
        Log.i("UserToken",UserToken);
        profile_image=LoginPrefrences.getString("profileImage","");
        first_nameuser=LoginPrefrences.getString("userFirstName","");
        last_nameuser=LoginPrefrences.getString("userLastName","");



        TextView txtTitle = (TextView) rowView.findViewById(R.id.compalint_row_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.complaint_row_img);



        txtTitle.setText(web[position]);
        imageView.setImageResource(img[position]);

       // Registration_preferences = context.getSharedPreferences("net.clamour.detlef.Profile.RegistrationScreen", MODE_PRIVATE);
        //UserName = Registration_preferences.getString("UserName", "");


        if(position==0){
            rowView=inflater.inflate(R.layout.profile_row,null,true);

            TextView name=(TextView)rowView.findViewById(R.id.name);
            ImageView profile=(ImageView)rowView.findViewById(R.id.drawer_profile_image);

            name.setText(first_nameuser+" "+last_nameuser);

            Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/"+profile_image)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile);




        }
        if(position==9){
            rowView=inflater.inflate(R.layout.switchlocation,null,true);



        }











        return rowView;
    }

}
