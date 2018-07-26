package net.clamour.mangalcity.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.clamour.mangalcity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenImageActivity extends AppCompatActivity {

    String postImage_st, postText_st;
    @BindView(R.id.post_image)
    ImageView postImage;
    @BindView(R.id.post_text)
    TextView post_text;
    @BindView(R.id.cross_image)
    ImageView crossImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        postImage_st = intent.getStringExtra("postimage");
        postText_st = intent.getStringExtra("posttext");

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Glide.with(OpenImageActivity.this).load("http://emergingncr.com/mangalcity/public/images/post/post_image/" + postImage_st)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(postImage);

        if (postText_st.isEmpty()) {

            post_text.setText("");
        } else {

            post_text.setText(postText_st);
        }
    }
}
