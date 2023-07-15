package com.example.civiladvocacy;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class IndividualListAdapter extends RecyclerView.Adapter<IndividualListViewHolder>{

    public ArrayList<IndividualListClass> offcAl;
    public MainActivity mainAct;
    private static final String TAG = "IndividualListAdapter";
    private static int num = 0;

    public IndividualListAdapter(ArrayList<IndividualListClass> offcAl, MainActivity main) {
        this.offcAl = offcAl;
        this.mainAct = main;
    }

    @NonNull
    @Override
    public IndividualListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_list, parent, false);
        itemView.setOnClickListener(mainAct);


        return new IndividualListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IndividualListViewHolder holder, int position) {
        IndividualListClass indList = offcAl.get(position);
        holder.name.setText(indList.name);
        holder.position.setText(indList.position);
            Log.d(TAG, "onBindViewHolder: Image Person "+ indList.name+" "+indList.imgURL);
            downloadImage(holder, indList.imgURL, holder.picture);
    }

    @Override
    public int getItemCount() {
        return offcAl.size();
    }


    // Image Download Code
    private boolean downloadImage(IndividualListViewHolder holder, String urlString, ImageView imageView) {
        long start = System.currentTimeMillis();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.missing)
                .error(R.drawable.brokenimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);


        Glide.with(imageView)
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
                .into(imageView);
        return true;
    }
    }

