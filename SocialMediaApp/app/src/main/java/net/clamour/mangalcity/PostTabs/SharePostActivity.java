package net.clamour.mangalcity.PostTabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.Home.PostActivity;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.PostShareresponse;
import net.clamour.mangalcity.webservice.ApiClient;
import net.clamour.mangalcity.webservice.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharePostActivity extends AppCompatActivity {


    private static final String TAG = "SharePostActivity";

    @BindView(R.id.post_text)
    TextView postText;
    @BindView(R.id.post_image)
    ImageView postImage;
    @BindView(R.id.post_video)
    VideoView postVideo;
    @BindView(R.id.share_post)
    Button sharePost;
    @BindView(R.id.profile_image)
    ImageView profile_image;

    String post_text_st, post_image_st, post_video_st, userToken, postId,profile_image_st;
    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess;
    @BindView(R.id.relative_imagevideo)
    RelativeLayout relativeImagevideo;
    @BindView(R.id.relative_complete)
    RelativeLayout relativeComplete;

    SharedPreferences LoginPrefrences;
    String pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);
        ButterKnife.bind(this);

        android.support.v7.widget.Toolbar toolbar1 = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("Share Post");


        setSupportActionBar(toolbar1);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LoginPrefrences = this.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        pro=LoginPrefrences.getString("profileImage","");
        Log.i("profile",pro);



        Intent intent = getIntent();
        post_text_st = intent.getStringExtra("text");
        post_image_st = intent.getStringExtra("image");
        post_video_st = intent.getStringExtra("video");
        userToken = intent.getStringExtra("token");
        postId = intent.getStringExtra("post_id");
        profile_image_st=intent.getStringExtra("profileimage");

        Glide.with(SharePostActivity.this).load("http://emergingncr.com/mangalcity/public/images/user/"+pro)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_image);



        if (post_image_st == null && post_video_st == null) {

            ViewGroup.LayoutParams params = relativeImagevideo.getLayoutParams();
            params.height = 0;
            params.width = 0;

            ViewGroup.LayoutParams params1 = relativeComplete.getLayoutParams();

            params1.height = 200;
            params1.width = 700;


            postText.setText(post_text_st);

        }

        else if(post_image_st==null){

            postVideo.setVisibility(View.VISIBLE);
            postImage.setVisibility(View.INVISIBLE);

            Uri uri = Uri.parse("http://emergingncr.com/mangalcity/public/images/post/post_video/" +post_video_st);
            postVideo.setVideoURI(uri);

            MediaController mediaController = new
                    MediaController(SharePostActivity.this);
            mediaController.setAnchorView(postVideo);
            postVideo.setMediaController(mediaController);
            postVideo.pause();
            postVideo.seekTo(100);

        }

        else if(post_video_st==null){

            postImage.setVisibility(View.VISIBLE);
            postVideo.setVisibility(View.INVISIBLE);

            Glide.with(SharePostActivity.this).load("http://emergingncr.com/mangalcity/public/images/post/post_image/"+post_image_st)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(postImage);

        }


        sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePostData();
            }
        });


    }

    public void sharePostData() {

        pDialog = new ProgressDialog(SharePostActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<PostShareresponse> call = apiInterface.share_post(userToken, postId);

        call.enqueue(new Callback<PostShareresponse>() {
            @Override
            public void onResponse(Call<PostShareresponse> call, Response<PostShareresponse> response) {
                pDialog.cancel();

                PostShareresponse postDeleteResponse = response.body();
                isSucess = postDeleteResponse.getSuccess();
                Log.d(TAG, "onResponse: " + isSucess);


                if (isSucess == true) {


                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            SharePostActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("                 Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("            Successfully Posted ");

                    // Setting Icon to Dialog


                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                            //                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            //                                    startActivity(intent);
                            // Write your code here to execute after dialog closed
                            // alertDialog.dismiss();
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_LONG).show();

                            // verifyEmail();
                            // saveData();
                            alertDialog.dismiss();

                            Intent intent=new Intent(SharePostActivity.this, PostActivity.class);
                            startActivity(intent);

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
//
//                } else if (isSucess == false) {
//
//                    final AlertDialog alertDialog = new AlertDialog.Builder(
//                            context).create();
//                    // saveData();
//                    // Setting Dialog Title
//                    alertDialog.setTitle("                 Alert!");
//
//                    // Setting Dialog Message
//                    alertDialog.setMessage("    Invalid Credentials");
//
//                    // Setting Icon to Dialog
//
//
//                    // Setting OK Button
//                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            //                                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                            //                                    emailIntent.setType("text/plain");
//                            //                                    startActivity(emailIntent);
//
//                            // Write your code here to execute after dialog closed
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                    // Showing Alert Message
//                    alertDialog.show();
//                }
            }

            @Override
            public void onFailure(Call<PostShareresponse> call, Throwable t) {

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


    }

}

