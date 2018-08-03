package net.clamour.mangalcity.PostTabs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        comment.setText(feedPostData.getMessage());

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