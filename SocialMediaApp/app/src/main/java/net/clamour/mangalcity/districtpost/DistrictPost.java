package net.clamour.mangalcity.districtpost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.PostTabs.CommonPostActivity;
import net.clamour.mangalcity.PostTabs.PostAdapter;
import net.clamour.mangalcity.PostTabs.PostModalClass;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.CountryPostResponse;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.LoginResponse;
import net.clamour.mangalcity.ResponseModal.UserPostResponse;
import net.clamour.mangalcity.profile.LoginActivity;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistrictPost extends AppCompatActivity {

    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess;
    private static final String TAG = "CountryPost";
    List<CountryPostResponse>countryPostResponses_array;
    //  ArrayList<PostModalClass> post_array;
    PostAdapter postAdapter;
    private RecyclerView recyclerView;
    SharedPreferences LoginPrefrences;
    String UserToken;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_post);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("District Post");


        setSupportActionBar(toolbar1);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken=LoginPrefrences.getString("userToken","");




        countryPostResponses_array=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
//        gallery_upload=(ImageView)findViewById(R.id.gallery_upload);
//        setImage=(ImageView)findViewById(R.id.setImage);
//        PostText_et=(EditText)findViewById(R.id.editText_textpost) ;
//        post_button=(Button)findViewById(R.id.post_button);
//
//        gallery_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //captureImage();
//            }
//        });
//
//
//

        getCountryPost();

    }

    public void getCountryPost(){

        pDialog = new ProgressDialog(DistrictPost.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<FeedsResponse> call = apiInterface.getFeeds(UserToken);

        call.enqueue(new Callback<FeedsResponse>() {
            @Override
            public void onResponse(Call<FeedsResponse> call, Response<FeedsResponse> response) {
                pDialog.cancel();

                FeedsResponse feedsResponse = response.body();
                isSucess = feedsResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);

                countryPostResponses_array=feedsResponse.getDistrict_posts();
                Log.d(TAG, "onResponse: " + countryPostResponses_array);


                for ( CountryPostResponse countryPostResponse:countryPostResponses_array){

                    String hfdh=countryPostResponse.getCreated_at();
                    Log.d(TAG, "onResponse: "+hfdh);

                    String fjdsfj=countryPostResponse.user.getFirst_name();
                    Log.d(TAG, "onResponse: "+fjdsfj);

                }
                postAdapter=new PostAdapter(DistrictPost.this,countryPostResponses_array);

                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(DistrictPost.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(postAdapter);


//                first_name = loginResponse.data.user.first_name;
//                Log.d(TAG, "onResponse: " + first_name);
//                last_name = loginResponse.data.user.last_name;
//                mobile = loginResponse.data.user.mobile;
//                email = loginResponse.data.user.email;



            }

            @Override
            public void onFailure(Call<FeedsResponse> call, Throwable t) {

            }
        });


    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                // adapter.notifyDataSetChanged();

                finish();
                // NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}