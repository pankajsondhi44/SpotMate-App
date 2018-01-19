package com.spotmate.spotmate;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class AboutUs extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView1, imageView2, imageView3, imageView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView1 = (ImageView) findViewById(R.id.ritesh);
        imageView2 = (ImageView) findViewById(R.id.pankaj);
        imageView3 = (ImageView) findViewById(R.id.yash);
        imageView4 = (ImageView) findViewById(R.id.raghav);

        imageView1.setImageResource(R.mipmap.logo);
        imageView2.setImageResource(R.mipmap.logo);
        imageView3.setImageResource(R.mipmap.logo);
        imageView4.setImageResource(R.mipmap.logo);

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Quicksand-Regular.otf");

        textView = (TextView) findViewById(R.id.textView4);
        textView.setTypeface(font);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            Log.e("AboutUs", "onCreate: " + e);
        }
    }
}
