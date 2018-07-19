package net.clamour.mangalcity.PostTabs;

/**
 * Created by clamour_5 on 7/19/2018.
 */




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

        import net.clamour.mangalcity.Activity.ActivityPost;
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
    private String post_id;
    private String userToken;
    private String user_id;
    private SharedPreferences LoginPrefrences;


    TextView status,time;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    private Context context;
    public List<ActivityPost> event_list;

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public MyViewHolder(View view) {
            super(view);

            status=(TextView)view.findViewById(R.id.status);
            time=(TextView)view.findViewById(R.id.time);




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
         ActivityPost activityPost = event_list.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                LoginPrefrences = context.getSharedPreferences("net.clamour.mangalcity.profile.LoginActivity", MODE_PRIVATE);
                userToken = LoginPrefrences.getString("userToken", "");
                Log.i("UserTokenAdapter", userToken);

                user_id = LoginPrefrences.getString("user_id", "");
                Log.i("userid", user_id);


                status.setText(activityPost.getMessage());



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


    public ActivityPost getItem(int position) {
        return event_list.get(position);
    }

    public void refreshAdapter(List<ActivityPost> newList) {
        event_list.clear();
        event_list.addAll(newList);
        notifyDataSetChanged();
    }

    public void add(ActivityPost r) {
        event_list.add(r);
        notifyItemInserted(event_list.size());
    }

    public void addAll(List<ActivityPost> moveResults) {
        for (ActivityPost result : moveResults) {
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
        add(new ActivityPost());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = event_list.size() - 1;
        ActivityPost result = getItem(position);

        if (result != null) {
            event_list.remove(position);
            notifyItemRemoved(position);
        }
    }



}
