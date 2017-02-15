package com.example.nunepc.beautyblinkbeautician.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nunepc.beautyblinkbeautician.R;
import com.squareup.picasso.Picasso;

/**
 * Created by NunePC on 14/2/2560.
 */

public class PlannerViewHolder  extends RecyclerView.ViewHolder  {

    public View mview;

    public PlannerViewHolder(View itemView){
        super(itemView);
        mview=itemView;

    }

    public void setTitle(String title){
        TextView set_title = (TextView) mview.findViewById(R.id.title);
        set_title.setText(title);
    }

    public void setStart(String start) {
        TextView set_start = (TextView) mview.findViewById(R.id.start);
        set_start.setText(start);
    }

    public void setEnd(String end) {
        TextView set_end = (TextView) mview.findViewById(R.id.end);
        set_end.setText(end);
    }


}
