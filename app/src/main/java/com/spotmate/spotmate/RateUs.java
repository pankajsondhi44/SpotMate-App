package com.spotmate.spotmate;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RateUs extends AppCompatActivity {

    private String db = "https://spotmate-feb1e.firebaseio.com/";

    private DatabaseReference databaseReference;
    private Button submit;
    private RatingBar ratingBar;
    private GoogleSignInAccount gsa;
    private String username;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Quicksand-Regular.otf");
        textView = (TextView) findViewById(R.id.textView5);
        textView.setTypeface(font);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(db);

        submit = (Button) findViewById(R.id.submit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        gsa = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        String email = gsa.getEmail().toString();
        Pattern pattern = Pattern.compile("(.*)@(.*)");
        Matcher matcher = pattern.matcher(email);

        if(matcher.find()) {
            username = matcher.group(1).replaceAll("\\.", "");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingBar.getNumStars() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select the number of stars before submitting", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("rating").child(username).setValue(ratingBar.getRating());
                    Toast.makeText(getApplicationContext(),"Thank you for rating us!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            Log.e("RateUs", "onCreate: " + e);
        }
    }

}
