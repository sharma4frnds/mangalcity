package net.clamour.mangalcity.PostTabs;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.CountryPostResponse;
import net.clamour.mangalcity.ResponseModal.FeedsResponse;
import net.clamour.mangalcity.ResponseModal.LogoutResponse;
import net.clamour.mangalcity.ResponseModal.SpamDataResponse;
import net.clamour.mangalcity.ResponseModal.SpamTagsResponse;
import net.clamour.mangalcity.countrypost.CountryPost;
import net.clamour.mangalcity.profile.LoginActivity;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends AppCompatActivity {

    ListView listView;
    List<SpamDataResponse> tags_array;
    FeedBackAdapter feedBackAdapter;
    ApiInterface apiInterface;
    String userToken;
    SharedPreferences LoginPrefrences;
    Boolean isSucess;
    private static final String TAG = "FeedBackActivity";
    ProgressDialog pDialog;
    Button feedback;
    String post_id,tag_id;
    SpamTagsResponse spamTagsResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        android.support.v7.widget.Toolbar toolbar1 = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("Report Spam");

        setSupportActionBar(toolbar1);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
      userToken=LoginPrefrences.getString("userToken","");

      Intent intent=getIntent();
      post_id=intent.getStringExtra("postid");

        feedback=(Button)findViewById(R.id.submitFeedback);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitFaeedback();
            }
        });

        listView=(ListView)findViewById(R.id.listview);
        tags_array=new ArrayList<>();
        feedBackAdapter=new FeedBackAdapter(FeedBackActivity.this,tags_array);
        listView.setAdapter(feedBackAdapter);
       getSpamTags();


    }



    public void getSpamTags(){
        pDialog = new ProgressDialog(FeedBackActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<SpamTagsResponse> call = apiInterface.getSpamTags(userToken);

        call.enqueue(new Callback<SpamTagsResponse>() {
            @Override
            public void onResponse(Call<SpamTagsResponse> call, Response<SpamTagsResponse> response) {
                pDialog.cancel();


                 spamTagsResponse = response.body();
                isSucess = spamTagsResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);

                tags_array = spamTagsResponse.getData();
                Log.d(TAG, "onResponse: " + tags_array);


                for (SpamDataResponse spamDataResponse : tags_array) {

                    String tagname = spamDataResponse.getName();
                    Log.d(TAG, "onResponse: " + tagname+""+spamDataResponse.getId());
                    tag_id=spamDataResponse.getId();




                }

                feedBackAdapter=new FeedBackAdapter(FeedBackActivity.this,tags_array);
                listView.setAdapter(feedBackAdapter);

            }


            @Override
            public void onFailure(Call<SpamTagsResponse> call, Throwable t) {

            }
        });


    }
public void SubmitFaeedback(){

    pDialog = new ProgressDialog(FeedBackActivity.this);
    pDialog.setMessage("Please wait...");
    pDialog.setCancelable(true);
    pDialog.show();


    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    Call<LogoutResponse> call = apiInterface.getReportFeedback(userToken,post_id,tag_id);

    call.enqueue(new Callback<LogoutResponse>() {
        @Override
        public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
            pDialog.cancel();

            LogoutResponse fogetResponse = response.body();
            isSucess = fogetResponse.getSuccess();
            Log.d(TAG, "onResponse: " + isSucess);


            if (isSucess == true) {

                Toast.makeText(getApplicationContext(),"Thankyou for your FeedBack",Toast.LENGTH_SHORT).show();

            } else if (isSucess == false) {

                Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();


            }
        }

        @Override
        public void onFailure(Call<LogoutResponse> call, Throwable t) {

        }
    });


}
    @Override

    public boolean onOptionsItemSelected(MenuItem item){
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

}}

