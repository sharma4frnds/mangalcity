package net.clamour.mangalcity.sidebar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import net.clamour.mangalcity.Home.DrawerBaseActivity;
import net.clamour.mangalcity.R;

public class AboutUs extends DrawerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar1.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar1.setTitle("About Us");
        setSupportActionBar(toolbar1);
        setDrawer();
    }
}
