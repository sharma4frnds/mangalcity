package net.clamour.mangalcity.profile;

/**
 * Created by clamour_5 on 6/19/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.clamour.mangalcity.R;

import java.util.ArrayList;

/**
 * Created by clamour_5 on 5/12/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.clamour.mangalcity.R;

import java.util.ArrayList;

public class GenderAdapter extends BaseAdapter {
    Context context;
    String[] countryNames;
  //  ArrayList<ModalClassName> countryDataStorages;
    LayoutInflater inflter;

    public GenderAdapter(Context applicationContext, String[] countryNames) {
        this.context = applicationContext;

        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);

        TextView names = (TextView) view.findViewById(R.id.textView);

        names.setText(countryNames[i]);
    //    names.setText(countryDataStorages.get(i).getStatename());
        return view;
    }
}