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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

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
    EditText postText;
    @BindView(R.id.post_image)
    ImageView postImage;
  //  @BindView(R.id.post_video)
  //  VideoView postVideo;
    @BindView(R.id.share_post)
    Button sharePost;
    @BindView(R.id.profile_image)
    ImageView profile_image;

    String post_text_st, post_image_st, post_video_st, userToken, postId,profile_image_st,post_audio_st;
    ProgressDialog pDialog;
    ApiInterface apiInterface;
    Boolean isSucess;
    @BindView(R.id.relative_imagevideo)
    RelativeLayout relativeImagevideo;
    @BindView(R.id.relative_complete)
    RelativeLayout relativeComplete;
    @BindView(R.id.audiorelative)
    RelativeLayout audiorelative;

    RelativeLayout videorelative;

    SimpleExoPlayerView exoPlayerView,exoPlayerView_audio;
    SimpleExoPlayer exoPlayer;

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
        pro = LoginPrefrences.getString("profileImage", "");
        Log.i("profile", pro);


        Intent intent = getIntent();
        post_text_st = intent.getStringExtra("text");
        Log.d(TAG, "onCreate: " + post_text_st);
        post_image_st = intent.getStringExtra("image");
        post_video_st = intent.getStringExtra("video");
        userToken = intent.getStringExtra("token");
        postId = intent.getStringExtra("post_id");
        Log.d(TAG, "onCreate: " + postId);
        profile_image_st = intent.getStringExtra("profileimage");
        post_audio_st=intent.getStringExtra("audio");
        Log.d(TAG, "onCreate: "+post_audio_st);

        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
        videorelative = (RelativeLayout) findViewById(R.id.videorelative);
        exoPlayerView_audio = (SimpleExoPlayerView) findViewById(R.id.exo_player_view_audio);


        Glide.with(SharePostActivity.this).load("http://emergingncr.com/mangalcity/public/images/user/" + pro)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_image);


        if (post_image_st == null && post_video_st == null && post_audio_st == null) {

            ViewGroup.LayoutParams params = relativeImagevideo.getLayoutParams();
            params.height = 0;
            params.width = 0;

            ViewGroup.LayoutParams params1 = relativeComplete.getLayoutParams();

            params1.height = 400;


            postText.setText(post_text_st);

        } else if (post_image_st == null && post_text_st != null) {

            postImage.setVisibility(View.INVISIBLE);
            videorelative.setVisibility(View.VISIBLE);
            postText.setText(post_text_st);

            try {

                String videoUrl = "http://emergingncr.com/mangalcity/public/images/post/post_video/" + post_video_st;
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(SharePostActivity.this, trackSelector);

                Uri videoURI = Uri.parse(videoUrl);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
            } catch (Exception e) {
                Log.e("SharePost", " exoplayer error " + e.toString());
            }

        } else if (post_video_st == null && post_text_st != null) {

            postImage.setVisibility(View.VISIBLE);
            videorelative.setVisibility(View.INVISIBLE);
            postText.setText(post_text_st);

            Glide.with(SharePostActivity.this).load("http://emergingncr.com/mangalcity/public/images/post/post_image/" + post_image_st)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(postImage);

        } else if (post_image_st == null && post_video_st == null && post_text_st != null) {

            postImage.setVisibility(View.INVISIBLE);
            videorelative.setVisibility(View.INVISIBLE);
            audiorelative.setVisibility(View.VISIBLE);
            postText.setText(post_text_st);

            try {
            String videoUrl = "http://emergingncr.com/mangalcity/public/images/post/post_audio/"+post_audio_st;
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(SharePostActivity.this, trackSelector);

            Uri videoURI = Uri.parse(videoUrl);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            exoPlayerView_audio.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);

        } catch(Exception e){
            Log.e("PostVideo", " exoplayer error " + e.toString());

        }
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

        post_text_st=postText.getText().toString();
        Log.d(TAG, "sharePostData: "+post_text_st+""+postId);


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<PostShareresponse> call = apiInterface.share_post(userToken,postId,post_text_st);

        call.enqueue(new Callback<PostShareresponse>() {
            @Override
            public void onResponse(Call<PostShareresponse> call, Response<PostShareresponse> response) {
                pDialog.cancel();

                try {
                    PostShareresponse postDeleteResponse = response.body();


                    isSucess = postDeleteResponse.getSuccess();
                    Log.d(TAG, "onResponse: " + isSucess);


                    if (isSucess == true) {

                        Toast.makeText(getApplicationContext(),"sucessfully posted",Toast.LENGTH_SHORT).show();


                    }
                }
                catch (Exception e){

                }
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
              Intent intent=new Intent(SharePostActivity.this,PostActivity.class);
                startActivity(intent);


                // adapter.notifyDataSetChanged();

              //  finish();
                // NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SharePostActivity.this,PostActivity.class);
        startActivity(intent);
    }
}

