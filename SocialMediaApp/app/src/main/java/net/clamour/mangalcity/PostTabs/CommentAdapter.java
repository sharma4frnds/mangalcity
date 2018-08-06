package net.clamour.mangalcity.PostTabs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.Home.GetTimeAgo;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.FeedPostData;
import net.clamour.mangalcity.feed.CommentShowData;
import net.clamour.mangalcity.feed.PostFeedResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clamour_5 on 7/30/2018.
 */

public class CommentAdapter extends ArrayAdapter<CommentShowData> {

     Activity context;


    List<CommentShowData> comment_array;
    public CommentAdapter(Activity context,
                      List<CommentShowData>comment_array) {
        super(context, R.layout.comment_list_items, comment_array);
        this.context = context;
        this.comment_array=comment_array;


    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.comment_list_items, null, true);
        CommentShowData feedPostData=comment_array.get(position);

        TextView comment=(TextView)rowView.findViewById(R.id.comment);
        TextView comment_time=(TextView)rowView.findViewById(R.id.comment_time) ;
        TextView user_name_comment=(TextView)rowView.findViewById(R.id.user_name_comment);

        comment.setText(feedPostData.getMessage());

        GetTimeAgo getTimeAgo = new GetTimeAgo();
        String time = feedPostData.getCreated_at();

        // long lastTime = Long.parseLong(post.getCreatedAt());

        String lastSeenTime = getTimeAgo.getTimeAgo(time, context);

        comment_time.setText(lastSeenTime);
        user_name_comment.setText(feedPostData.getFirst_name()+" "+feedPostData.getLast_name());

        ImageView user_image=(ImageView)rowView.findViewById(R.id.user_image);

        Glide.with(context).load("http://emergingncr.com/mangalcity/public/images/user/" +feedPostData.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(user_image);

       // comment.setText(feedPostData.getMessage());


//        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
//
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
//        txtTitle.setText(web[position]);
//
//        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}