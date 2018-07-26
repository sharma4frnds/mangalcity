package net.clamour.mangalcity.facebookplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.clamour.mangalcity.R;

import uk.co.jakelee.vidsta.VidstaPlayer;

public class VideoPlaying extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);
//
//        VidstaPlayer player = (VidstaPlayer) findViewById(R.id.player);
//        player.setVideoSource("http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4");
//        player.setAutoLoop(true);
//        player.setAutoPlay(true);
    }
}
