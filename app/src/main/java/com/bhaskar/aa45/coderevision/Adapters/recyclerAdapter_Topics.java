package com.bhaskar.aa45.coderevision.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bhaskar.aa45.coderevision.Firebase.DataHolder;
import com.bhaskar.aa45.coderevision.MeFragment;
import com.bhaskar.aa45.coderevision.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class recyclerAdapter_Topics extends RecyclerView.Adapter<recyclerAdapter_Topics.ViewHolder> {
    Context context;
    HashMap<String,Integer> hm;
    List<DataHolder> allItems;
    private recyclerViewAdapter viewAdapter ;
    private SwipeRefreshLayout swipeRefreshLayout;


    public recyclerAdapter_Topics(Context context,HashMap<String,Integer> hm,List<DataHolder> allItems) {
        this.context = context;
        this.hm = hm;
        this.allItems=allItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_grid_layout, parent, false);
        return new recyclerAdapter_Topics.ViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String key = (String) hm.keySet().toArray()[position];
        holder.topic_grid.setText(key);
        Integer freq = hm.get(key);
        int fr = freq==null?0:freq;
        String str = "Solved :  "+fr;
        holder.totalNo.setText(str);

        holder.gridCard.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context,R.style.Base_Theme_CodeRevision);
            dialog.setContentView(R.layout.dialog_topics_sections);

            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_topics);
            ImageView backTopic = dialog.findViewById(R.id.back_to_topic);
            TextView topicName = dialog.findViewById(R.id.topic_name);
            LinearLayout parent = dialog.findViewById(R.id.parent_select_topic);

            List<DataHolder> sortedList = new ArrayList<>();

            for (DataHolder item : allItems){


                String[] tags = item.getTag().split("    ",0);

                for (String tag : tags){
                    if(Objects.equals(tag, key)){
                        sortedList.add(item);
                        break;
                    }
                }


            }
            topicName.setText(key);

            backTopic.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            if((MeFragment.check==-1 && AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO)
                    ||(MeFragment.check==0)){
                topicName.setTextColor(context.getResources().getColor(R.color.black));
                backTopic.setImageDrawable(context.getResources().getDrawable(R.drawable.back_arrow_black));
                parent.setBackgroundColor(context.getResources().getColor(R.color.white));
            }else{
                topicName.setTextColor(context.getResources().getColor(R.color.white));
                backTopic.setImageDrawable(context.getResources().getDrawable(R.drawable.back_arrow));
                parent.setBackgroundColor(context.getResources().getColor(R.color.primary));
            }

            viewAdapter = new recyclerViewAdapter(sortedList,context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(viewAdapter);

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return hm.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView totalNo ;
        public TextView topic_grid ;
        public CardView gridCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            totalNo = itemView.findViewById(R.id.totalNo);
            topic_grid = itemView.findViewById(R.id.topic_grid);
            gridCard = itemView.findViewById(R.id.GridCard);
        }
    }



}
