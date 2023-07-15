package com.example.civiladvocacy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

public class officialActivity extends AppCompatActivity {

    TextView location;
    TextView name;
    TextView position;
    TextView party;
    ImageView displayPictue;
    ImageView facebook;
    ImageView twitter;
    ImageView youtube;
    ImageView partylogo;
    TextView address;
    TextView email;
    TextView phone;
    TextView website;

    String twitter_name = "";
    String facebook_name = "";
    String youtube_name = "";
    String locationString;
    IndividualListClass ivlVal;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_activity);

         location = findViewById(R.id.locationString);
         position = findViewById(R.id.position);
         name = findViewById(R.id.name);
         party = findViewById(R.id.party);
         displayPictue = findViewById(R.id.displayPicture);
         facebook = findViewById(R.id.facebook);
         twitter = findViewById(R.id.twitter);
         youtube = findViewById(R.id.youtube);
         partylogo = findViewById(R.id.partyEmbelem);
         address = findViewById(R.id.address);
         email = findViewById(R.id.email);
         phone = findViewById(R.id.phone);
         website = findViewById(R.id.website);

         if(savedInstanceState!=null)
         {
             IndividualListClass ivl = (IndividualListClass) savedInstanceState.getSerializable("individualListClass");
             locationString = savedInstanceState.getString("location");
             ivlVal = ivl;
             setData(ivl);
         } else {
             Intent intent = getIntent();
             IndividualListClass ivl = (IndividualListClass) intent.getSerializableExtra("individualListClass");
             locationString = intent.getStringExtra("location");
             ivlVal = ivl;
             setData(ivl);
         }

       activityResultLauncher = registerForActivityResult
                (new ActivityResultContracts.StartActivityForResult(), this::handleResult);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("location", locationString);
        outState.putSerializable("individualListClass", ivlVal);
        super.onSaveInstanceState(outState);
    }

    private <O> void handleResult(O o) {
    }

    public void setData(IndividualListClass ivl){

        name.setText(ivl.name);
        position.setText(ivl.position);
        party.setText("("+ivl.party+")");
        twitter_name = ivl.twitter;
        facebook_name = ivl.facebook;
        youtube_name = ivl.youtube;
        location.setText(locationString);

        if(twitter_name.equalsIgnoreCase(""))
            twitter.setVisibility(View.GONE);
        if(facebook_name.equalsIgnoreCase(""))
            facebook.setVisibility(View.GONE);
        if(youtube_name.equalsIgnoreCase(""))
            youtube.setVisibility(View.GONE);

        if(!ivl.address.equals("")) {
            address.setText(ivl.address);
            address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else {
            address.setVisibility(View.GONE);
            findViewById(R.id.address_label).setVisibility(View.GONE);
        }

        if(!ivl.email.equals("")) {
            email.setText(ivl.email);
            email.setPaintFlags(email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else {
            email.setVisibility(View.GONE);
            findViewById(R.id.email_label).setVisibility(View.GONE);
        }

        if(!ivl.website.equals("")) {
            website.setText(ivl.website);
            website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else {
            website.setVisibility(View.GONE);
            findViewById(R.id.website_label).setVisibility(View.GONE);
        }

        if(!ivl.phone.equals("")) {
            phone.setText(ivl.phone);
            phone.setPaintFlags(phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else {
            phone.setVisibility(View.GONE);
            findViewById(R.id.phone_label).setVisibility(View.GONE);
        }

        if(ivl.party.equalsIgnoreCase("democratic party")) {
            partylogo.setImageDrawable(getDrawable(R.drawable.dem_logo));
            findViewById(R.id.mainLayout).setBackgroundColor(Color.parseColor("#0000fe"));
        }
        else if(ivl.party.equalsIgnoreCase("republican party")) {
            partylogo.setImageDrawable(getDrawable(R.drawable.rep_logo));
            findViewById(R.id.mainLayout).setBackgroundColor(Color.parseColor("#fe0000"));
        }
        else {
            partylogo.setVisibility(View.INVISIBLE);
            findViewById(R.id.mainLayout).setBackgroundColor(Color.parseColor("#000000"));
        }


        downloadImage(ivl.imgURL);
    }

    private boolean downloadImage(String urlString) {
        long start = System.currentTimeMillis();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.missing)
                .error(R.drawable.brokenimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);


        Glide.with(displayPictue)
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
                .into(displayPictue);
        return true;
    }

    public void onClickTwitter(View view){
        Intent intent;
        String name = twitter_name;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void onClickFacebook(View view){
        Intent intent;
        String name = facebook_name;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.facebook.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("facebook://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/" + name));
        }
        startActivity(intent);
    }

    public void onClickYouTube(View view){
        Intent intent;
        String name = youtube_name;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.youtube.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("youtube://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/" + name));
        }
        startActivity(intent);
    }
    
    public void onClickImage(View view){
        if(!ivlVal.imgURL.equalsIgnoreCase("")) {
            Intent intent = new Intent(this, photo_activity.class);
            intent.putExtra("individualListClass", ivlVal);
            intent.putExtra("location", locationString);
            activityResultLauncher.launch(intent);
        }
    }

    public void onClickAddress(View view){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+ivlVal.address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void onClickPhone(View view){
        String uri = "tel:" + ivlVal.phone.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public void onClickEmail(View view){

        Uri uri = Uri.parse("mailto:" + ivlVal.email)
                .buildUpon()
                .build();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(emailIntent);
    }

    public void onClickWebSite(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ivlVal.website));
        startActivity(intent);
    }

    public void onClickParty(View view){
        String url = "";
        if(ivlVal.party.equalsIgnoreCase("democratic party"))
            url = "https://democrats.org/";
        else if(ivlVal.party.equalsIgnoreCase("republican party"))
            url="https://www.gop.com";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}