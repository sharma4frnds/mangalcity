package net.clamour.mangalcity.PostTabs;

/**
 * Created by clamour_5 on 7/19/2018.
 */




        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.MediaController;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.VideoView;

        import com.androidnetworking.AndroidNetworking;
        import com.androidnetworking.error.ANError;
        import com.androidnetworking.interfaces.DownloadListener;
        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;

        import net.clamour.mangalcity.Activity.ActivityData;
        import net.clamour.mangalcity.Activity.ActivityPost;
        import net.clamour.mangalcity.Home.OpenImageActivity;
        import net.clamour.mangalcity.Home.OtherUserProfile;
        import net.clamour.mangalcity.Home.PostActivity;
        import net.clamour.mangalcity.R;
        import net.clamour.mangalcity.ResponseModal.CityPostResponse;
        import net.clamour.mangalcity.ResponseModal.CountryPostResponse;
        import net.clamour.mangalcity.ResponseModal.DislikeResponse;
        import net.clamour.mangalcity.ResponseModal.LikeResponse;
        import net.clamour.mangalcity.ResponseModal.LoginResponse;
        import net.clamour.mangalcity.ResponseModal.PostDeleteResponse;
        import net.clamour.mangalcity.profile.LoginActivity;
        import net.clamour.mangalcity.webservice.ApiClient;
        import net.clamour.mangalcity.webservice.ApiInterface;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;

        import butterknife.BindView;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        import android.net.Uri;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;

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


        import static android.content.Context.MODE_PRIVATE;


public class ActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ActivityAdapter";


    ItemClickListener clickListener;

    private String username,gender;
    private String userToken;
    private String user_id;
    private SharedPreferences LoginPrefrences;


    TextView message,createdtime,user_name,sharepost;
    RelativeLayout relative_image,relative_video,relative_audio;
    ImageView postImage;
    private SimpleExoPlayer exoPlayer;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    private Context context;
    public List<ActivityData> event_list;
 //   public List<ActivityData>activityData;
 @BindView(R.id.exo_player_view)
 SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.exo_player_view_audio)
    SimpleExoPlayerView exoPlayerView_audio;

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public MyViewHolder(View view) {
            super(view);

            message=(TextView)view.findViewById(R.id.message);
            createdtime=(TextView)view.findViewById(R.id.created_time);
            relative_video=(RelativeLayout)view.findViewById(R.id.videorelative);
            relative_audio=(RelativeLayout)view.findViewById(R.id.audiorelative);
            relative_image=(RelativeLayout)view.findViewById(R.id.relative_image);
            postImage=(ImageView) view.findViewById(R.id.post_image);
            sharepost=(TextView)view.findViewById(R.id.share_post);


            view.setTag(view);

            view.setOnClickListener(this);




        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public ActivityAdapter(Context context) {
        this.context = context;
        event_list = new ArrayList<>();

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
         ActivityData activityData = event_list.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                LoginPrefrences = context.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
                userToken = LoginPrefrences.getString("userToken", "");
                Log.i("UserTokenAdapter", userToken);

                user_id = LoginPrefrences.getString("user_id", "");
                Log.i("userid", user_id);



                if (activityData.post.getType().equalsIgnoreCase("video")) {

                    Log.i("video", "video");

                    relative_image.setVisibility(View.INVISIBLE);
                    relative_video.setVisibility(View.VISIBLE);
                    relative_audio.setVisibility(View.INVISIBLE);

                    try {

                        String videoUrl = "http://emergingncr.com/mangalcity/public/images/post/post_video/" + activityData.post.getValue();
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                        Uri videoURI = Uri.parse(videoUrl);

                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                        exoPlayerView.setPlayer(exoPlayer);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(false);

                    } catch (Exception e) {
                        Log.e("PostVideo", " exoplayer error " + e.toString());
                    }


                } else if (activityData.post.getType().equalsIgnoreCase("image")) {

                    Log.i("image", "image");

                    relative_image.setVisibility(View.VISIBLE);
                    relative_video.setVisibility(View.INVISIBLE);
                    relative_audio.setVisibility(View.INVISIBLE);
                   // Log.d(TAG, "onBindViewHolder: "+activityData.post.);


                    Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/post/post_image/" + activityData.post.getValue())
                            .thumbnail(0.5f)
                            .crossFade()
                            .placeholder(R.drawable.anu)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(postImage);


//                    postImage.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            Intent intent = new Intent(context, OpenImageActivity.class);
//                            intent.putExtra("postimage", post.getValue());
//                            intent.putExtra("posttext", post.getMessage());
//
//                            context.startActivity(intent);
//
//
//                        }
//                    });


                } else if (activityData.post.getType().equalsIgnoreCase("audio")) {

                    Log.i("audio", "audio");

                    relative_image.setVisibility(View.INVISIBLE);
                    relative_video.setVisibility(View.INVISIBLE);
                    relative_audio.setVisibility(View.VISIBLE);

                    try {

                        String videoUrl = "http://emergingncr.com/mangalcity/public/images/post/post_audio/" + activityData.post.getValue();
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                        Uri videoURI = Uri.parse(videoUrl);

                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                        exoPlayerView_audio.setPlayer(exoPlayer);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(false);
                    } catch (Exception e) {
                        Log.e("PostVideo", " exoplayer error " + e.toString());
                    }


                } else if (activityData.post.getType().equalsIgnoreCase("")) {

                    relative_image.setVisibility(View.INVISIBLE);
                    relative_video.setVisibility(View.VISIBLE);
                    relative_audio.setVisibility(View.INVISIBLE);

//                    ViewGroup.LayoutParams params = relativ1.getLayoutParams();
//                    params.height = 0;
//                    params.width = 0;
//
//                    RelativeLayout innerLayout2 = new RelativeLayout(context);
//                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1f);
//                    innerLayout2.setLayoutParams(layoutParams2);
//
//
//                    ViewGroup.LayoutParams params1 = relativeImage.getLayoutParams();
//
//                    params1.height = 540;
                    // params1.width = 1000;


                }




               createdtime.setText(activityData.post.getCreated_at());
                message.setText(activityData.post.getMessage());
                gender=activityData.post.getUser().getGender();

                if(activityData.getType()=="comment" && gender=="female"){
sharepost.setText(activityData.post.getUser().getFirstName()+" "+activityData.post.getUser().getLastName()+"commented on her Own Status");

                }
                else if(activityData.getType()=="comment" && gender=="male"){
                    sharepost.setText(activityData.post.getUser().getFirstName()+" "+activityData.post.getUser().getLastName()+"commented on his Own Status");

                }
                else if (activityData.getType()=="like"){

                }
                else if (activityData.getType()=="dislike"){

                }

                else if (activityData.getType()=="post"){

                }



                break;
            case LOADING:
//                Do nothing
                break;

        }


    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.activityitems, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }
    @Override
    public int getItemCount() {
        return event_list == null ? 0 : event_list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (position == event_list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;


    }


    public ActivityData getItem(int position) {
        return event_list.get(position);
    }

    public void refreshAdapter(List<ActivityData> newList) {
        event_list.clear();
        event_list.addAll(newList);
        notifyDataSetChanged();
    }

    public void add(ActivityData r) {
        event_list.add(r);
        notifyItemInserted(event_list.size());
    }

    public void addAll(List<ActivityData> moveResults) {
        for (ActivityData result : moveResults) {
            add(result);
        }
    }

    public void remove(CityPostResponse r) {
        int position = event_list.indexOf(r);
        if (position > -1) {
            event_list.remove(position);
            notifyItemRemoved(position);
        }
    }

//    public void clear() {
//        isLoadingAdded = false;
//        while (getItemCount() > 0) {
//            remove(getItem(0));
//        }
////        if(event_list.size()<0){
////            event_list.clear();
////        }
//    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ActivityData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = event_list.size() - 1;
        ActivityData result = getItem(position);

        if (result != null) {
            event_list.remove(position);
            notifyItemRemoved(position);
        }
    }



}
