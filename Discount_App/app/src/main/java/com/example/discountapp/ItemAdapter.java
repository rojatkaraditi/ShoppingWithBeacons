package com.example.discountapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private static final String TAG = "okay_itemAdapter";
    ArrayList<Item> items =  new ArrayList<>();
    Activity activity;

    public ItemAdapter(ArrayList<Item> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        Log.d(TAG, "onBindViewHolder: in rv adapter item =>"+item);
        holder.item_name.setText(item.name);
        holder.item_discount.setText(item.discount+"");
        holder.item_price.setText(item.price+"");
        holder.item_discount_price.setText(item.discountPrice);
        holder.item_region.setText(item.region);
        if (item.region.equals(activity.getString(R.string.grocery_tag)))
            holder.item_region.setTextColor(activity.getResources().getColor(R.color.green));
        if (item.region.equals(activity.getString(R.string.life_style_tag)))
            holder.item_region.setTextColor(activity.getResources().getColor(R.color.orange));
        if (item.region.equals(activity.getString(R.string.produce_tag)))
            holder.item_region.setTextColor(activity.getResources().getColor(R.color.red));
        if (!holder.isImageLoaded){
            String url_str = activity.getString(R.string.image_url);
            Picasso.get().load(url_str+item.photo).into(holder.itemImg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_name,item_discount,item_price,item_discount_price,item_region;
        ImageView itemImg;
        boolean isImageLoaded = false;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_discount = itemView.findViewById(R.id.item_discount);
            item_discount_price = itemView.findViewById(R.id.item_discounted_price);
            item_region = itemView.findViewById(R.id.item_region);
            item_price = itemView.findViewById(R.id.item_price);
            item_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            itemImg = itemView.findViewById(R.id.item_image);
        }
    }

}
