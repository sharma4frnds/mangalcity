package net.clamour.mangalcity.Home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import net.clamour.mangalcity.PostTabs.ActivityLog;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.LogoutResponse;
import net.clamour.mangalcity.profile.LoginActivity;
import net.clamour.mangalcity.profile.UserProfile;
import net.clamour.mangalcity.sidebar.AboutUs;
import net.clamour.mangalcity.sidebar.ChangePassword;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawerBaseActivity extends AppCompatActivity {
    ActionBarDrawerToggle actionBarDrawerToggle;
    ProgressDialog pDialog;
    ApiInterface apiInterface;
    String UserToken;
    SharedPreferences LoginPrefrences;
    Boolean isSucess;
    private static final String TAG = "DrawerBaseActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);

        setDrawer();

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken=LoginPrefrences.getString("userToken","");
        Log.i("UserToken",UserToken);




    }

    public void setDrawer(){
        ListView lt = (ListView) findViewById(R.id.left_drawer);
        String St[] = {"","Home","My Profile","Activity Log","Rate Us","About Us","Share","Change Password","Logout",""};
        Integer imgs[] = {0,R.drawable.home,R.drawable.ic_person_black_24dp,R.drawable.activitylog,R.drawable.rating,R.drawable.about,R.drawable.share,R.drawable.security,R.drawable.ic_exit_to_app_black_24dp,0};


        CustomAdapterDrawer CAD = new CustomAdapterDrawer(this, St, imgs);
        lt.setAdapter(CAD);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        android.support.v7.widget.Toolbar toolbar1 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        // toolbar1.setTitleTextColor(Color.WHITE);
        //toolbar1.setTitle("My Healthy Host");



        lt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(position==1){

                    Intent intent=new Intent(DrawerBaseActivity.this,PostActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                if(position==2){
                    Intent intent=new Intent(DrawerBaseActivity.this,UserProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                if(position==3){

                    Intent intent=new Intent(DrawerBaseActivity.this,ActivityLog.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                if(position==7){
                    Intent intent=new Intent(DrawerBaseActivity.this,ChangePassword.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                if(position==4){

                    rateUs();
                }
//
                if(position==5){
                    Intent intent=new Intent(DrawerBaseActivity.this,AboutUs.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                if(position==6){
                    shareApp();

                }


                if(position==8){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            DrawerBaseActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("                    Logout!");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("      Are you really want to logout")
                            .setCancelable(false)
                            .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    // login_prefrence.edit().remove("outh_id").apply();
                                    // Registration_preferences.edit().remove("outh_id").apply();
//                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                                    intent.addCategory(Intent.CATEGORY_HOME);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    finish();

                                    logOutApp();

                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();

                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();




                }

            }
        });



        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar1,
                R.string.app_name,
                R.string.app_name
        )

        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //Set the custom toolbar
        if (toolbar1 != null) {
            setSupportActionBar(toolbar1);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle.syncState();



    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;

        }
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void rateUs(){
        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id="
                        + DrawerBaseActivity.this.getPackageName()));
        startActivity(intent1);

    }
    public void shareApp(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void logOutApp(){


        pDialog = new ProgressDialog(DrawerBaseActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<LogoutResponse> call = apiInterface.userLogout(UserToken);

        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                pDialog.cancel();

                LogoutResponse loginResponse = response.body();
                isSucess = loginResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);


                if (isSucess == true) {

                     LoginPrefrences.edit().remove("userToken").apply();
                                    Intent intent = new Intent(DrawerBaseActivity.this,LoginActivity.class);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();




                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {

            }
        });


    }}
