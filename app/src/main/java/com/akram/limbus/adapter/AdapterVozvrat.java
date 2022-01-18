package com.akram.limbus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.akram.limbus.dialog.Class_change_adapter_bottom_sheet_vozvrat;
import com.akram.limbus.R;
import com.akram.limbus.model.CategorySee;
import com.akram.limbus.model.CategoryVozvrat;

import java.util.ArrayList;
import java.util.List;

public class AdapterVozvrat extends RecyclerView.Adapter<AdapterVozvrat.SeeViewHolder>{
    Context context;
    List<CategorySee> categorySeeList;
    public static List<CategoryVozvrat> categoryVozvratList = new ArrayList<>();

    public AdapterVozvrat(Context context, List<CategorySee> categorySeeList) {
        this.context = context;
        this.categorySeeList = categorySeeList;
    }

    @NonNull
    @Override
    public SeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newItems = LayoutInflater.from(context).inflate(R.layout.item_for_vozv, parent, false);
        Log.d("my", "onCreate");
        for (int i = 0; i < categorySeeList.size(); i++){
            categoryVozvratList.add(new CategoryVozvrat(
                    categorySeeList.get(i).getId(),
                    1,
                    Integer.parseInt(categorySeeList.get(i).getQuantity()),
                    false));
        }
        return new AdapterVozvrat.SeeViewHolder(newItems);
    }

    @Override
    public void onBindViewHolder(@NonNull SeeViewHolder holder, int position) {
        Log.d("my", "onBind");

        holder.name.setText(categorySeeList.get(position).getName());
        holder.select_quantity.setText(categoryVozvratList.get(position).getSelect_count() + "");
        holder.total_quantity.setText(categorySeeList.get(position).getQuantity());
        holder.price.setText(categorySeeList.get(position).getTotal_price());
        double tot_pr = Double.parseDouble(categorySeeList.get(position).getTotal_price()) *
                categoryVozvratList.get(position).getSelect_count();
        holder.total_price.setText(tot_pr + "");

        holder.btn_minus.setOnClickListener(v -> {
            if (categoryVozvratList.get(position).getSelect_count() > 1){
                categoryVozvratList.get(position).setSelect_count(categoryVozvratList.get(position).getSelect_count() - 1);
                Class_change_adapter_bottom_sheet_vozvrat.change_adapter_bottom_sheet_vozvrat();
            }
        });
        holder.btn_plus.setOnClickListener(v -> {
            if (categoryVozvratList.get(position).getSelect_count() < categoryVozvratList.get(position).getTotal_count()){
                categoryVozvratList.get(position).setSelect_count(categoryVozvratList.get(position).getSelect_count() + 1);
                Class_change_adapter_bottom_sheet_vozvrat.change_adapter_bottom_sheet_vozvrat();
            }
        });
        if (holder.checkBox.isChecked()){
            holder.name.setTextColor(Color.parseColor("#FF000000"));
        }else
            holder.name.setTextColor(Color.parseColor("#74747C"));

        holder.btn_minus.setEnabled(holder.checkBox.isChecked());
        holder.btn_plus.setEnabled(holder.checkBox.isChecked());

        holder.checkBox.setOnClickListener(v -> {
            categoryVozvratList.get(position).setChecked(holder.checkBox.isChecked());
            holder.btn_minus.setEnabled(holder.checkBox.isChecked());
            holder.btn_plus.setEnabled(holder.checkBox.isChecked());
            if (holder.checkBox.isChecked()){
                holder.name.setTextColor(Color.parseColor("#FF000000"));
            }else
                holder.name.setTextColor(Color.parseColor("#74747C"));
        });
    }

    @Override
    public int getItemCount() {
        return categorySeeList.size();
    }

    public static final class SeeViewHolder extends RecyclerView.ViewHolder{

        TextView name, select_quantity, total_quantity, price, total_price;
        Button btn_minus, btn_plus;
        CheckBox checkBox;

        public SeeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            select_quantity = itemView.findViewById(R.id.select_quantity);
            total_quantity = itemView.findViewById(R.id.total_quantity);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);

            price = itemView.findViewById(R.id.price);
            total_price = itemView.findViewById(R.id.total_price);
            checkBox = itemView.findViewById(R.id.checkBox);

            Log.d("my", "ViewHolder");

        }
    }

}
