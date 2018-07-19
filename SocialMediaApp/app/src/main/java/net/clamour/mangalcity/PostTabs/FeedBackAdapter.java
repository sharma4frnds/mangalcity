package net.clamour.mangalcity.PostTabs;

/**
 * Created by clamour_5 on 6/28/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import net.clamour.mangalcity.R;
import net.clamour.mangalcity.ResponseModal.SpamDataResponse;
import net.clamour.mangalcity.ResponseModal.SpamTagsResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import static android.content.ContentValues.TAG;

/**
 * Created by clamour_5 on 1/2/2018.
 */

public class FeedBackAdapter extends ArrayAdapter<SpamDataResponse> {


    public final Activity context;
    ItemClickListener itemClickListener;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.label)
    TextView label;
    private int selectedPosition = -1;

    List<SpamDataResponse> feedback_array;


    public FeedBackAdapter(Activity context, List<SpamDataResponse> feedback_array) {
        super(context, R.layout.feedbackitems, feedback_array);

        this.feedback_array = feedback_array;
        this.context = context;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.feedbackitems, null, true);

        ButterKnife.bind(this, view);

        SpamDataResponse spamDataResponse = feedback_array.get(position);
        String name=spamDataResponse.getName();
        Log.d(TAG, "getView: "+name);
        label.setText(spamDataResponse.getName());

        radioButton1.setChecked(position == selectedPosition);

        //Set the position tag to both radio button and label
        radioButton1.setTag(position);
        label.setTag(position);

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
        });

        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }


        });
        return view;
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        Log.i("selectedposition",selectedPosition+"");
        notifyDataSetChanged();
       // Log.i(selectedId)feedback_array.get(selectedPosition).getId();

    }



//    private class ViewHolder {
//        private TextView label;
//        private RadioButton radioButton;
//    }
//
//    //Return the selectedPosition item
//    public String getSelectedItem() {
//        if (selectedPosition != -1) {
//            Toast.makeText(context, "Selected Item : " + feedback_array.get(selectedPosition), Toast.LENGTH_SHORT).show();
//            return ;
//        }
//        return "";
//    }



  }