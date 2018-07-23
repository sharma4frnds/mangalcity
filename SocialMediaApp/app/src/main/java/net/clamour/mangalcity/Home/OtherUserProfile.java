package net.clamour.mangalcity.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.StateMainResponse;
import net.clamour.mangalcity.feed.FeedPostData;
import net.clamour.mangalcity.feed.PostFeedResponse;
import net.clamour.mangalcity.statepost.StatePost;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherUserProfile extends AppCompatActivity {


    SharedPreferences LoginPrefrences;
    String UserToken, user_url, user_image_st, user_coverImage_st, first_name_st, last_name_st,email_st,maritalstatus_st,address_st,gender_st,dob_st,profession_st,mobile_st;
    ProgressBar progressBar;
    @BindView(R.id.coverImage_other)
    ImageView coverImageOther;
    @BindView(R.id.person_profile_image)
    CircleImageView personProfileImage;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.dob)
    TextView dob;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.marital)
    TextView marital;
    @BindView(R.id.profession)
    TextView profession;
    @BindView(R.id.view11)
    View view11;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.user_name)
    TextView userName;
    ProgressDialog pDialog;
    ApiInterface apiInterface;
    // @BindView(R.id.user_name)
    //  TextView userName;

    String search_send;
    Boolean isSucess;
    private static final String TAG = "OtherUserProfile";
    List<FeedPostData>data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        ButterKnife.bind(this);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("Profile");


        setSupportActionBar(toolbar1);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        UserToken = LoginPrefrences.getString("userToken", "");
        data=new ArrayList<>();




        Intent intent = getIntent();
        user_url = intent.getStringExtra("user_url");
        Log.d(TAG, "onCreate: "+user_url);

        search_send=intent.getStringExtra("bypost");

        if(search_send.equals("searchsend")){

            OtherUserProfileApi();

        }

        else if(search_send.equals("postsend"))
        {

            user_image_st = intent.getStringExtra("user_image");
            user_coverImage_st = intent.getStringExtra("user_cover_image");
            first_name_st = intent.getStringExtra("first_name");
            last_name_st = intent.getStringExtra("last_name");
            email_st=intent.getStringExtra("email");
            dob_st=intent.getStringExtra("dob");
            profession_st=intent.getStringExtra("profession");
            gender_st=intent.getStringExtra("gender");
            mobile_st=intent.getStringExtra("mobile");
            maritalstatus_st=intent.getStringExtra("marital");
            address_st=intent.getStringExtra("address");

        }



        userName.setText(first_name_st + " " + last_name_st);
        email.setText(email_st);
        mobile.setText(mobile_st);
        profession.setText(profession_st);
        gender.setText(gender_st);
        marital.setText(maritalstatus_st);
        address.setText(address_st);
        dob.setText(dob_st);



        Glide.with(OtherUserProfile.this).load("http://emergingncr.com/mangalcity/public/images/user/" + user_image_st)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(personProfileImage);

        Glide.with(OtherUserProfile.this).load("http://emergingncr.com/mangalcity/public/images/user/cover/" + user_coverImage_st)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(coverImageOther);

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

    public void OtherUserProfileApi(){

        pDialog = new ProgressDialog(OtherUserProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<PostFeedResponse> call = apiInterface.getOtherProfile(UserToken,user_url);

        call.enqueue(new Callback<PostFeedResponse>() {
            @Override
            public void onResponse(Call<PostFeedResponse> call, Response<PostFeedResponse> response) {
                pDialog.cancel();

                PostFeedResponse feedsResponse = response.body();
                isSucess = feedsResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);

                data=feedsResponse.getCityPosts().getData();
                Log.d(TAG, "onResponse: " + data);

                for(FeedPostData feedPostData:data){

                    email_st=feedPostData.getUser().getEmail();
                    Log.d(TAG, "onResponse: "+email_st);
                    mobile_st=feedPostData.getUser().getMobile();
                    dob_st=feedPostData.getUser().getDob();
                    gender_st=feedPostData.getUser().getGender();
                    maritalstatus_st=feedPostData.getUser().getMaritalStatus();
                    profession_st=feedPostData.getUser().getProfession();
                    first_name_st=feedPostData.getUser().getFirstName();
                    last_name_st=feedPostData.getUser().getLastName();
                    user_coverImage_st=feedPostData.getUser().getCoverImage();
                    user_image_st=feedPostData.getUser().getImage();
                    address_st=feedPostData.getUser().getAddress();


                }




                userName.setText(first_name_st + " " + last_name_st);
                email.setText(email_st);
                mobile.setText(mobile_st);
                profession.setText(profession_st);
                gender.setText(gender_st);
                marital.setText(maritalstatus_st);
                address.setText(address_st);
                dob.setText(dob_st);

                Glide.with(OtherUserProfile.this).load("http://emergingncr.com/mangalcity/public/images/user/" + user_image_st)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(0)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(personProfileImage);

                Glide.with(OtherUserProfile.this).load("http://emergingncr.com/mangalcity/public/images/user/cover/" + user_coverImage_st)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(0)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(coverImageOther);




            }

            @Override
            public void onFailure(Call<PostFeedResponse> call, Throwable t) {

            }
        });


    }





}






