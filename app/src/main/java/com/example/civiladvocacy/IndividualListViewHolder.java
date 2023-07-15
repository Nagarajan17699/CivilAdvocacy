package com.example.civiladvocacy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IndividualListViewHolder extends RecyclerView.ViewHolder{

    ImageView picture;
    TextView position;
    TextView name;

    public IndividualListViewHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.individualPic);
        name = itemView.findViewById(R.id.individualName);
        position = itemView.findViewById(R.id.individualPosition);
    }
}
