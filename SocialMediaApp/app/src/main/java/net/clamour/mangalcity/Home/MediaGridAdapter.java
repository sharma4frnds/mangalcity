package net.clamour.mangalcity.Home;

/**
 * Created by clamour_5 on 8/3/2018.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.R;
import net.clamour.mangalcity.feed.FeedPostData;
import net.clamour.mangalcity.feed.MediaImageResponse;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MediaGridAdapter extends ArrayAdapter<MediaImageResponse>{
    private Context mContext;

    List<MediaImageResponse>feedPostData;
    private static final String TAG = "MediaGridAdapter";

    public MediaGridAdapter(Context c,List<MediaImageResponse> feedPostData) {
        super(c, R.layout.comment_list_items, feedPostData);
        this.mContext = c;
        this.feedPostData=feedPostData;

    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: "+feedPostData.size());
        // TODO Auto-generated method stub
        return feedPostData.size();


    }

//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return null;
//    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.mediaitems, null);

            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);

            MediaImageResponse mediaImageResponse=feedPostData.get(position);
            String imagename=mediaImageResponse.getName();
            Log.d(TAG, "getViewimagename: "+imagename);

            Glide.with(mContext).load("http://emergingncr.com/mangalcity/public/images/post/post_image/" +mediaImageResponse.getName())
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);


        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
