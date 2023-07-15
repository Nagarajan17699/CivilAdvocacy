package com.example.civiladvocacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class photo_activity extends AppCompatActivity {

    private static final String TAG = "photo_activity";
    TextView location;
    TextView name;
    TextView position;
    ImageView displayPicture;
    ImageView partyLogo;
    IndividualListClass ivl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail_activity);

        location = findViewById(R.id.location);
        name = findViewById(R.id.name_in_pic);
        position = findViewById(R.id.position_in_pic);
        displayPicture = findViewById(R.id.displayPic);
        partyLogo  = findViewById(R.id.partylogo2);

        Intent intent = getIntent();
        location.setText(intent.getStringExtra("location"));
        IndividualListClass ivlObj = (IndividualListClass) intent.getSerializableExtra("individualListClass");
        ivl = ivlObj;

        name.setText(ivl.name);
        position.setText(ivl.position);
        downloadImage(ivl.imgURL);


        Log.d(TAG, "onCreate: "+ ivl.party);
        if(ivl.party.equalsIgnoreCase("democratic party")) {
            partyLogo.setImageDrawable(getDrawable(R.drawable.dem_logo));
            Log.d(TAG, "onCreate: Inside dem party ");
            findViewById(R.id.photoActivityLayout).setBackgroundColor(Color.parseColor("#0000fe"));
        }
        else if(ivl.party.equalsIgnoreCase("republican party")) {
            partyLogo.setImageDrawable(getDrawable(R.drawable.rep_logo));
            findViewById(R.id.photoActivityLayout).setBackgroundColor(Color.parseColor("#fe0000"));
        }
        else {
            Log.d(TAG, "onCreate: Inside Else");
            partyLogo.setVisibility(View.INVISIBLE);
            findViewById(R.id.photoActivityLayout).setBackgroundColor(Color.parseColor("#000000"));
        }

    }


    private boolean downloadImage(String urlString) {
        long start = System.currentTimeMillis();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.missing)
                .error(R.drawable.brokenimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);


        Glide.with(displayPicture)
                .load((!urlString.equals("")) ? urlString : R.drawable.missing)
                .apply(options)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Log.d(TAG, "onLoadFailed: " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        long time = System.currentTimeMillis() - start;
                        //Log.d(TAG, "onResourceReady: " + time);
                        return false;
                    }
                })
                .into(displayPicture);
        return true;
    }


}