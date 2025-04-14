package com.example.printtoorder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> implements Filterable {
    private static final String TAG = ShoppingAdapter.class.getName();

    private ArrayList<Item> mItemsData;
    private ArrayList<Item> mItemsDataAll;
    private Context context;
    private int lastPosition = -1;

    ShoppingAdapter(Context context, ArrayList<Item> itemsData){
        this.mItemsData = itemsData;
        this.mItemsDataAll = itemsData;
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return mItemsData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.main_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem = mItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getBindingAdapterPosition() > lastPosition){
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.in_row);
            holder.itemView.startAnimation(anim);
            lastPosition = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public Filter getFilter() {
        return shopFilter;
    }

    private Filter shopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Item> filtered = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0){
                results.count = mItemsDataAll.size();
                results.values = mItemsDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Item item: mItemsDataAll){
                    if (item.getName().toLowerCase().contains(filterPattern)) filtered.add(item);

                }
                results.count = filtered.size();
                results.values = filtered;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mItemsData = (ArrayList<Item>) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private TextView textDescription;

        public ViewHolder(View itemView){
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);

            itemView.findViewById(R.id.buttonToCart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Add cart button clicked");
                }
            });
        }

        public void bindTo(Item currentItem){
            textTitle.setText(currentItem.getName());
            textDescription.setText(currentItem.getDescription());
        }
    }
}

