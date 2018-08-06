package net.clamour.mangalcity.PostTabs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import net.clamour.mangalcity.Activity.ActivityData;
import net.clamour.mangalcity.Activity.ActivityPost;
import net.clamour.mangalcity.Home.GetTimeAgo;
import net.clamour.mangalcity.Home.MediaGridAdapter;
import net.clamour.mangalcity.Home.OpenImageActivity;
import net.clamour.mangalcity.Home.OtherUserProfile;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.feed.FeedPostData;
import net.clamour.mangalcity.feed.MediaImageResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import nl.changer.audiowife.AudioWife;
import uk.co.jakelee.vidsta.VidstaPlayer;

import static android.content.Context.MODE_PRIVATE;


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PostAdapter";


    private String post_id;
    private String userToken;
    private String user_id;
    private SharedPreferences LoginPrefrences;
    private SimpleExoPlayer exoPlayer,exoPlayerView_audio;
    private static final int FORMPOST = 0;
    private static final int ITEM = 1;
    private static final int LOADING = 2;
    android.support.v7.app.AlertDialog alertDialog;
    ProgressDialog pDialog;
    String  profileimage_get;
    List<MediaImageResponse> mediaList;


    private boolean isLoadingAdded = false;

    private Context context;
    private List<FeedPostData> feedPostData;
    private OnItemClickListner itemClickListner;
    private OnPostClickListner onPostClickListner;
    private String profile_image;
    private Uri uri;
    private MyPostViewHolder myPostViewHolder;
    private String mediaImagename;


    public interface OnItemClickListner {
        void onLikeClick(int position);

        void onDislikeClick(int position);

        void onSharePostClick(int position);

        void onShareImageClick(int position);

        void onDotClick(int position);

        void onUserImageClick(int position);

        void onCommentImageClick(int position);

        void onShareTextClick(int position);

        void OnCommentTextClick(int position);





    }

    public interface OnPostClickListner {
        void onPostButtonClick(String msz);

        void onImageButttonClick();

        void onVideoButttonClick();

        void onAudioButtonClick();

        void OnCrossImageClick();
    }

    public void setOnPostClickListner(OnPostClickListner postClickListner) {
        this.onPostClickListner = postClickListner;
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.itemClickListner = onItemClickListner;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public class MyPostViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.editText_textpost)
        EditText editTextTextpost;
        @BindView(R.id.person_profile_image)
        ImageView personProfileImage;
        @BindView(R.id.gallery_upload)
        ImageView galleryUpload;
        @BindView(R.id.video_upload)
        ImageView video_upload;
        @BindView(R.id.audio_upload)
        ImageView audio_upload;
        @BindView(R.id.imagepreview)
        ImageView imagepreview;
        @BindView(R.id.post_buttonnn)
         Button post_button;
        @BindView(R.id.cross_image)
        ImageView cross_image;
        @BindView(R.id.selected_file)
        TextView selected_file;

        private OnPostClickListner postClickListner;

        public MyPostViewHolder(View itemView, final OnPostClickListner listner) {
            super(itemView);
            this.postClickListner = listner;
            ButterKnife.bind(this, itemView);
//            Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/" + profile_image)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .placeholder(0)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(personProfileImage);


            getProfileData();

        }

        @OnClick({R.id.gallery_upload, R.id.video_upload,R.id.audio_upload, R.id.post_buttonnn,R.id.cross_image})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.gallery_upload:
                    postClickListner.onImageButttonClick();
                    if (uri != null)
                        imagepreview.setImageURI(null);
                    break;
                case R.id.video_upload:
                    postClickListner.onVideoButttonClick();
                    break;
                case R.id.audio_upload:
                    postClickListner.onAudioButtonClick();
                    break;

                case R.id.post_buttonnn:
                    String message = editTextTextpost.getText().toString();
                    postClickListner.onPostButtonClick(message);
                   // editTextTextpost.getText().clear();

                    if (message != null)
                        editTextTextpost.getText().clear();
                    break;

                case R.id.cross_image:
                    postClickListner.OnCrossImageClick();
            }
        }

        private void bindFormData(Uri uri) {
            imagepreview.setImageURI(uri);
        }

        private void resetFormData() {
            imagepreview.setImageResource(0);
selected_file.setVisibility(View.GONE);
cross_image.setVisibility(View.GONE);
            notifyItemChanged(0);
        }
public void disableButton(){


}

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.post_image)
//        ImageView postImage;
//        @BindView(R.id.audiolayout)
//        LinearLayout audiolayout;
        @BindView(R.id.exo_player_view_audio)
        SimpleExoPlayerView exoPlayerView_audio;
        @BindView(R.id.videoplayer)
        VidstaPlayer player;
        @BindView(R.id.gridView)
        GridView gridView;
//        @BindView(R.id.videorelative)
//        RelativeLayout videorelative;
//        @BindView(R.id.relativ1)
//        RelativeLayout relativ1;
//        @BindView(R.id.view)
//        View view;
        @BindView(R.id.image_like)
        ImageView imageLike;
        @BindView(R.id.image_dislike)
        ImageView imageDislike;
        @BindView(R.id.image_comment)
        ImageView imageComment;
        @BindView(R.id.image_share)
        ImageView imageShare;
        @BindView(R.id.no_of_likes)
        TextView noOfLikes;
        @BindView(R.id.no_of_dislikes)
        TextView noOfDislikes;
//        @BindView(R.id.textView11)
//        TextView textView11;
        @BindView(R.id.share_text)
        TextView shareText;
        @BindView(R.id.user_image)
        CircleImageView userImage;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.post_timing)
        TextView postTiming;
        @BindView(R.id.post_text)
        TextView postText;
        @BindView(R.id.commentsCountTextView)
        TextView comment_count;
//        @BindView(R.id.dots)
//        ImageView dots;
//        @BindView(R.id.play)
//        View mPlayMedia;
//        @BindView(R.id.pause)
//        View mPauseMedia;
//        @BindView(R.id.media_seekbar)
//        SeekBar mMediaSeekBar;
//        @BindView(R.id.run_time)
//        TextView mRunTime ;
//        @BindView(R.id.total_time)
//        TextView mTotalTime;




//        @BindView(R.id.relative_image)
//        RelativeLayout relativeImage;
//        @BindView(R.id.audiorelative)
//        RelativeLayout audiorelative;

        private OnItemClickListner listner;

        @OnClick({R.id.image_like, R.id.image_dislike, R.id.image_comment, R.id.image_share, R.id.no_of_likes, R.id.no_of_dislikes, R.id.share_text, R.id.dots ,R.id.user_name,R.id.commentsCountTextView})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.image_like:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onLikeClick(position-1);
                        }

                    }
                    break;
                case R.id.image_dislike:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onDislikeClick(position-1);
                        }

                    }
                    break;
                case R.id.image_comment:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onCommentImageClick(position-1);
                        }

                    }
                    break;
                case R.id.image_share:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onShareImageClick(position-1);
                        }

                    }
                    break;
                case R.id.no_of_likes:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onLikeClick(position-1);
                        }

                    }
                    break;
                case R.id.no_of_dislikes:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onDislikeClick(position-1);
                        }

                    }
                    break;
                case R.id.share_text:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onShareTextClick(position-1);
                        }

                    }
                    break;
                case R.id.dots:
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onDotClick(position-1);
                        }

                    }
                    break;


                case R.id.user_name:
                    if(listner !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.onUserImageClick(position-1);
                        }
                    }
                case R.id.commentsCountTextView:
                    if(listner !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnCommentTextClick(position-1);
                        }
                    }
            }
        }

        public MyViewHolder(View view, final OnItemClickListner onItemClickListner) {
            super(view);
            this.listner = onItemClickListner;
            ButterKnife.bind(this, view);

        }

        public void bind(final FeedPostData post) {
            noOfLikes.setText(post.getLikes() + "");
            noOfDislikes.setText(post.getDislikes() + "");

            if (post.getLikes() > 0)
                imageLike.setImageResource(R.drawable.like);
            else
                imageLike.setImageResource(R.drawable.like_gray);
            if (post.getDislikes() > 0)
                imageDislike.setImageResource(R.drawable.dislike);
            else
                imageDislike.setImageResource(R.drawable.dislike_grey);

            Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/" + post.getUser().getImage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(userImage);


//            userImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(user_id.equals(feedPostData.get(position).getUser_id())){
//
//                        userImage.setEnabled(false);
//                    }
//                    else {
//
//                        Intent intent=new Intent(context, OtherUserProfile.class);
//                        intent.putExtra("user_url",event_list.get(position).user.getUrl());
//                        intent.putExtra("user_image",event_list.get(position).user.getImage());
//                        intent.putExtra("user_cover_image",event_list.get(position).user.getCover_image());
//                        intent.putExtra("first_name",event_list.get(position).user.getFirst_name());
//                        intent.putExtra("last_name",event_list.get(position).user.getLast_name());
//
//                        context.startActivity(intent);
//                    }
//                }
//            });

            userName.setText(post.getUser().getFullName());
            post_id = post.getId() + "";
            postText.setText(post.getMessage());
            // postTiming.setText(post.getCreatedAt());
            Log.d(TAG, "bind: " + post.getUser().getImage());


            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String time = post.getCreatedAt();

            // long lastTime = Long.parseLong(post.getCreatedAt());

            String lastSeenTime = getTimeAgo.getTimeAgo(time, context);

            postTiming.setText(lastSeenTime);


            if (post.getType().contains("video")) {

                Log.i("video", "video");

                gridView.setVisibility(View.GONE);
                player.setVisibility(View.VISIBLE);
                exoPlayerView_audio.setVisibility(View.GONE);

                try {
                    player.setVideoSource("http://emergingncr.com/mangalcity/public/images/post/post_video/" + post.getValue());
                    player.setAutoLoop(false);
                    player.setAutoPlay(false);
                    player.setFullScreenButtonVisible(false);
                    // player.setFullScreen(true);
                    //   player.setFullScreenButtonVisible(true);
                } catch (Exception e) {


                }

//                videorelative.setVisibility(View.VISIBLE);
//                audiorelative.setVisibility(View.INVISIBLE);
//
//                try {
//
//                    String videoUrl = "http://emergingncr.com/mangalcity/public/images/post/post_video/" + post.getValue();
//                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//                    exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
//
//                    Uri videoURI = Uri.parse(videoUrl);
//
//                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
//                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//                    MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
//
//                    exoPlayerView.setPlayer(exoPlayer);
//                    exoPlayer.prepare(mediaSource);
//                    exoPlayer.setPlayWhenReady(false);
//
//                } catch (Exception e) {
//                    Log.e("PostVideo", " exoplayer error " + e.toString());
//                }


            } else if (post.getType().contains("image")) {

//                List<FeedPostData> list = post.getMedia();
//                List<MediaImageResponse> poatList = new ArrayList<>();

//                for(FeedPostData data : feedPostData){
//                    poatList.add(data.getMedia());
//                }

//                List<MediaImageResponse>mediaImageResponses=new ArrayList<>();
//
//                feedPostData=new ArrayList<>();
//                for(FeedPostData feedPostData1:feedPostData){
//
//                    //mediaImageResponses.add(feedPostData1.getMedia().get())
//                    feedPostData.add(feedPostData1);
//
//                }
//
//
//                                for (int i = 0; i < feedPostData.size(); i++) {
//                    String idddd = feedPostData.get(i).getMedia().get(i).getId();
//                                    Log.d(TAG, "bind: "+idddd);
//
//
//
//
//                }

                gridView.setVisibility(View.VISIBLE);
                // postImage.setVisibility(View.VISIBLE);
                player.setVisibility(View.GONE);
                exoPlayerView_audio.setVisibility(View.GONE);
                //  mediaList=new ArrayList<>();
                List<MediaImageResponse>mediaList=post.getMedia();


//                try {
//                    for (int i = 0; i < feedPostData.size(); i++) {
//                        mediaList = feedPostData.get(i).getMedia();
//
//                        if (mediaList != null) {
//                            for (MediaImageResponse media : mediaList) {
//
//                                mediaImagename = media.getName();
//                                Log.d(TAG, "bindimageeee: " + mediaImagename);
//
////                                if (media.getName() == null) {
////
////                                    mediaList.remove(mediaImagename);
////                                }
//
//                                mediaList.add(media);
//                            }
//
//                        }
//
//                    }
//                } catch (java.util.ConcurrentModificationException exception) {
//                    // Catch ConcurrentModificationExceptions.
//                    // Logging.log(exception);
//                    Log.d(TAG, "bind: " + exception);
//                } catch (Throwable throwable) {
//                    // Catch any other Throwables.
//                    // Logging.log(throwable);
//                }


                MediaGridAdapter adapter = new MediaGridAdapter(context,mediaList);

                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the GridView selected/clicked item text
                        String selectedItem = parent.getItemAtPosition(position).toString();

                        //   Toast.makeText(context,selectedItem,Toast.LENGTH_SHORT).show();

                        // Display the selected/clicked item text and position on TextView
                        Intent intent = new Intent(context, OpenImageActivity.class);
                        intent.putExtra("postimage", post.getMedia().get(position).getName());
                        intent.putExtra("posttext", post.getMessage());

                        context.startActivity(intent);


                    }
                });


                Log.i("image", "image");

//                videorelative.setVisibility(View.INVISIBLE);
//                audiorelative.setVisibility(View.INVISIBLE);


//                Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/post/post_image/" + post.getValue())
//                        .thumbnail(0.5f)
//                        .crossFade()
//                        .placeholder(R.drawable.anu)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(postImage);


//                postImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent intent = new Intent(context, OpenImageActivity.class);
//                        intent.putExtra("postimage", post.getValue());
//                        intent.putExtra("posttext", post.getMessage());
//
//                        context.startActivity(intent);
//
//
//                    }
//                });


            } else if (post.getType().contains("audio")) {

                Log.i("audio", "audio");

                gridView.setVisibility(View.GONE);
                player.setVisibility(View.GONE);
                exoPlayerView_audio.setVisibility(View.VISIBLE);


//                try {
//                    AudioWife.getInstance()
//                            .init(context,"http://emergingncr.com/mangalcity/public/images/post/post_audio/" + post.getValue())
//                            .setPlayView(mPlayMedia)
//                            .setPauseView(mPauseMedia)
//                            .setSeekBar(mMediaSeekBar)
//                            .setRuntimeView(mRunTime)
//                            .setTotalTimeView(mTotalTime);
//
//                }
//                catch (Exception e){
//
//
//                }


//                videorelative.setVisibility(View.INVISIBLE);
//                audiorelative.setVisibility(View.VISIBLE);

                try {

                    String videoUrl = "http://emergingncr.com/mangalcity/public/images/post/post_audio/" + post.getValue();
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


            } else if (post.getType().equalsIgnoreCase("")) {
                gridView.setVisibility(View.GONE);
                player.setVisibility(View.GONE);
                exoPlayerView_audio.setVisibility(View.GONE);

              //  postImage.setVisibility(View.GONE);
//                videorelative.setVisibility(View.GONE);
//                audiorelative.setVisibility(View.GONE);

//                ViewGroup.LayoutParams params = relativ1.getLayoutParams();
//                params.height = 0;
//                params.width = 0;
//
//                RelativeLayout innerLayout2 = new RelativeLayout(context);
//                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1f);
//                innerLayout2.setLayoutParams(layoutParams2);


              //  ViewGroup.LayoutParams params1 = relativeImage.getLayoutParams();

               // params1.height = 540;
                // params1.width = 1000;


            }

        }}

    public PostAdapter(Context context) {
        this.context = context;
        feedPostData = new ArrayList<>();
        LoginPrefrences = context.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
        userToken = LoginPrefrences.getString("userToken", "");
        user_id = LoginPrefrences.getString("user_id", "");
        profile_image = LoginPrefrences.getString("profileImage", "");

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case FORMPOST:
                viewHolder = getPostViewHolder(parent, inflater);
                break;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case FORMPOST:
                myPostViewHolder = (MyPostViewHolder) holder;

                break;
            case ITEM:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                //Log.d(TAG, "onBindViewHolder: "+position);
                FeedPostData feedPostDataList = feedPostData.get(position-1);
                //Log.d(TAG, "onBindViewHolder: "+feedPostDataList.toString());
                myViewHolder.bind(feedPostDataList);
                break;
            case LOADING:
//                Do nothing
                break;

        }


    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.newpostitems, parent, false);
        viewHolder = new MyViewHolder(v1, itemClickListner);
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getPostViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.form_layout, parent, false);
        viewHolder = new MyPostViewHolder(v1, onPostClickListner);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return feedPostData == null ? 0 : feedPostData.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FORMPOST;
        }
        if (position == feedPostData.size() - 1 && isLoadingAdded) {
            return LOADING;
        } else {
            return ITEM;
        }

    }


    public FeedPostData getItem(int position) {
        return feedPostData.get(position);
    }

    public void refreshAdapter(List<FeedPostData> newList) {
        isLoadingAdded = true;
        //restForm();
        feedPostData.clear();
        feedPostData.addAll(newList);
        notifyDataSetChanged();
    }

    public void add(FeedPostData r) {
        feedPostData.add(r);
        notifyItemInserted(feedPostData.size() - 1);
        //notifyDataSetChanged();
    }

    public void replace(int position , FeedPostData r) {
        feedPostData.set(position,r);
        notifyItemChanged(position+1);
        // notifyDataSetChanged();
    }
    public void addAll(List<FeedPostData> moveResults) {
        for (FeedPostData result : moveResults) {
            add(result);
        }
    }

    public void remove(FeedPostData r) {
        int position = feedPostData.indexOf(r);
        if (position > -1) {
            feedPostData.remove(position);
            notifyItemRemoved(position+1);
            //notifyDataSetChanged();
        }
    }

    public void clear() {

//        isLoadingAdded = false;
//        while (getItemCount() > 0) {
//            remove(getItem(0));
//        }

    }
public void setSelectionData(){

                       // myPostViewHolder.imagepreview.setImageURI(uri);
                   myPostViewHolder.selected_file.setVisibility(View.VISIBLE);
    myPostViewHolder.selected_file.setText("Image Selected");
    myPostViewHolder.cross_image.setVisibility(View.VISIBLE);
    myPostViewHolder.cross_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            myPostViewHolder.imagepreview.setImageResource(0);
                            myPostViewHolder.cross_image.setVisibility(View.INVISIBLE);
                            myPostViewHolder.selected_file.setVisibility(View.INVISIBLE);
                            myPostViewHolder.post_button.setEnabled(false);

                        }
                    });
}

public void setSelectionDataVideo(){
    myPostViewHolder.selected_file.setVisibility(View.VISIBLE);
    myPostViewHolder.selected_file.setText("Video Selected");
    myPostViewHolder.cross_image.setVisibility(View.VISIBLE);
    myPostViewHolder.cross_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {



            myPostViewHolder.cross_image.setVisibility(View.INVISIBLE);
            myPostViewHolder.selected_file.setVisibility(View.INVISIBLE);
            myPostViewHolder.post_button.setEnabled(false);

        }
    });

}

    public void setSelectionDataAudio(){

        myPostViewHolder.selected_file.setVisibility(View.VISIBLE);
        myPostViewHolder.selected_file.setText("Audio Selected");
        myPostViewHolder.cross_image.setVisibility(View.VISIBLE);
        myPostViewHolder.cross_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                myPostViewHolder.cross_image.setVisibility(View.INVISIBLE);
                myPostViewHolder.selected_file.setVisibility(View.INVISIBLE);
                myPostViewHolder.post_button.setEnabled(false);

            }
        });

}
    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new FeedPostData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = feedPostData.size() - 1;
        FeedPostData result = getItem(position);

        if (result != null) {
            feedPostData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setImageUri(Uri url) {
        this.uri = url;
        myPostViewHolder.bindFormData(uri);
    }
public void desableButton(){
//myPostViewHolder.post_button.setEnabled(false);
myPostViewHolder.editTextTextpost.requestFocus();
myPostViewHolder.editTextTextpost.setCursorVisible(true);
}
public void enableButton(){
    myPostViewHolder.post_button.setEnabled(true);
}
    public void restForm() {
        this.uri = null;
        myPostViewHolder.resetFormData();
    }
public void getProfileData(){

        pDialog = new ProgressDialog(context);
    pDialog.setMessage("Please wait...");
    pDialog.setCancelable(true);


    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://emergingncr.com/mangalcity/api/getprofile",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG).show();

                    Log.i("responsegetProfile", response);


                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    Log.e("response=", response);


                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Boolean isSucessget = jsonObject.getBoolean("success");






                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
//                       firstname_get = jsonObject1.getString("first_name");
//                        Log.d(TAG, "onResponsefirsttt: " + firstname_get);
//                      lastname_get = jsonObject1.getString("last_name");

                        profileimage_get = jsonObject1.getString("image");


                    } catch (Exception e) {
                    }
                    setData();

                    // saveUpdatedData();



                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    Log.i("errorr", error.toString());
                }
            })

    {


        @Override
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("token", userToken);


            return params;
        }

    };

    RequestQueue requestQueue1 = Volley.newRequestQueue(context);
    requestQueue1.add(stringRequest1);


}
  public void setData(){
    Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/" + profileimage_get)
            .thumbnail(0.5f)
            .crossFade()
            .placeholder(0)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(myPostViewHolder.personProfileImage);





}}
