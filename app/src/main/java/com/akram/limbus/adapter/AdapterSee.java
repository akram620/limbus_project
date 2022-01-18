package com.akram.limbus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akram.limbus.R;
import com.akram.limbus.model.CategorySee;

import java.util.List;

public class AdapterSee extends RecyclerView.Adapter<AdapterSee.SeeViewHolder>{
    Context context;
    List<CategorySee> categorySeeList;

    public AdapterSee(Context context, List<CategorySee> categorySeeList) {
        this.context = context;
        this.categorySeeList = categorySeeList;
    }

    @NonNull
    @Override
    public SeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("my", "cr  see");
        View newItems = LayoutInflater.from(context).inflate(R.layout.list_items_see, parent, false);
        return new AdapterSee.SeeViewHolder(newItems);
    }

    @Override
    public void onBindViewHolder(@NonNull SeeViewHolder holder, int position) {
        Log.d("my", "cr  Bind");
        holder.name.setText(categorySeeList.get(position).getName());
        holder.name.setSelected(true);
        holder.quantity.setText(categorySeeList.get(position).getQuantity());
        holder.code.setText(categorySeeList.get(position).getCode());
        holder.articul.setText(categorySeeList.get(position).getArticul());
        holder.total_price.setText(categorySeeList.get(position).getTotal_price());
    }

    @Override
    public int getItemCount() {
        return categorySeeList.size();
    }

    public static final class SeeViewHolder extends RecyclerView.ViewHolder{

        TextView name, quantity, code, articul, total_price;

        public SeeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            code = itemView.findViewById(R.id.code);
            articul = itemView.findViewById(R.id.articul);
            total_price = itemView.findViewById(R.id.total_price);
        }
    }
}
