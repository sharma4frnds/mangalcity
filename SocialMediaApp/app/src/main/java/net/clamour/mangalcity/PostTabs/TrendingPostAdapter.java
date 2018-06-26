package net.clamour.mangalcity.PostTabs;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.clamour.mangalcity.R;

import java.util.ArrayList;

public class TrendingPostAdapter extends PagerAdapter{
    Context context;
    int images[];
    LayoutInflater layoutInflater;



    ArrayList<PostModalClass> event_list;
    ItemClickListener clickListener;
    //  @BindView(R.id.post_image)
    ImageView postImage;
    //  @BindView(R.id.image_like)
    ImageView imageLike;
    //@BindView(R.id.image_dislike)
    ImageView imageDislike;
    //  @BindView(R.id.image_comment)
    ImageView imageComment;
    //  @BindView(R.id.image_share)
    ImageView imageShare;
    // @BindView(R.id.user_image)
    ImageView userImage;
    // @BindView(R.id.user_name)
    TextView userName;
    //@BindView(R.id.post_timing)
    TextView postTiming;
    //  @BindView(R.id.post_text)
    TextView postText;

    public TrendingPostAdapter(Context context, ArrayList<PostModalClass> event_list) {
        this.context = context;
        this.event_list = event_list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return event_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.postlistitems, container, false);

        notifyDataSetChanged();

        userName=(TextView)view.findViewById(R.id.user_name);
        postText=(TextView)view.findViewById(R.id.post_text);
        postTiming=(TextView)view.findViewById(R.id.post_timing);
        userImage=(ImageView)view.findViewById(R.id.user_image);
        imageShare=(ImageView)view.findViewById(R.id.image_share);
        imageComment=(ImageView)view.findViewById(R.id.image_comment);
        imageDislike=(ImageView)view.findViewById(R.id.image_dislike);
        imageLike=(ImageView)view.findViewById(R.id.image_like);
        postImage=(ImageView)view.findViewById(R.id.post_image);

        PostModalClass postModalClass = event_list.get(position);

        userName.setText(postModalClass.getUser_name());
        userImage.setImageResource(postModalClass.getUser_image());
        postImage.setImageResource(postModalClass.getPost_image());
        imageLike.setImageResource(postModalClass.getLike_image());
        imageDislike.setImageResource(postModalClass.getDislike_image());
        imageShare.setImageResource(postModalClass.getShare_image());
        imageComment.setImageResource(postModalClass.getComment_image());
        postText.setText(postModalClass.getPost_text());
        postTiming.setText(postModalClass.getPost_timing());



        container.addView(view);


        //listening to image click
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

