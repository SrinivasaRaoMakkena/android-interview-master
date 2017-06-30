package com.foursquare.takehome.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.foursquare.takehome.R;
import com.foursquare.takehome.model.VisitingPersonDuringTime;

import java.util.List;

/**
 * Adapter class to create the views and bind the data to the views in the form of list
 */

final public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    List<VisitingPersonDuringTime> visitorsListWithIdleTimes;
    RelativeLayout relativeLayout;

    public PersonAdapter(List<VisitingPersonDuringTime> list){
        this.visitorsListWithIdleTimes = list;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.venue_row_item, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (visitorsListWithIdleTimes.get(position).getName()=="No Visitors"){
                relativeLayout.setBackgroundColor(Color.CYAN);
                holder.visitorName.setTextColor(Color.BLACK);
                holder.visitingTime.setTextColor(Color.BLACK);
        }
        holder.visitorName.setText(visitorsListWithIdleTimes.get(position).getName());
        holder.visitingTime.setText(visitorsListWithIdleTimes.get(position).getInterval());
    }

    @Override
    public int getItemCount() {
        return visitorsListWithIdleTimes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView visitorName,visitingTime;

        public ViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutForEachRow);
            visitorName = (TextView) view.findViewById(R.id.visitorName);
            visitingTime = (TextView) view.findViewById(R.id.visitingTime);

        }
    }


}
