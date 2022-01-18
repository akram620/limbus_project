package com.akram.limbus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akram.limbus.model.AboutUser;
import com.akram.limbus.dialog.DialogSheet;
import com.akram.limbus.activity.Main_page;
import com.akram.limbus.R;
import com.akram.limbus.usecase.Smena;
import com.akram.limbus.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    Main_page main_page;
    DialogSheet dialogSheet;

    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItems = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new CategoryViewHolder(categoryItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        dialogSheet = new DialogSheet(context);
        holder.type_buyer.setText(categories.get(position).getType_buyer());
        holder.viruchka.setText(categories.get(position).getViruchka());
        holder.sum.setText(categories.get(position).getSum());
        holder.info_icon.setOnClickListener(v -> {
            AdapterVozvrat.categoryVozvratList.clear();
            if (Smena.getCheck()){
                holder.info_icon.setVisibility(View.GONE);
                holder.info_progress.setVisibility(View.VISIBLE);
                dialogSheet.seeDefinite(
                        context,
                        categories.get(position).getId(),
                        holder.info_progress,
                        holder.info_icon
                );

                AboutUser.check_na_summu = categories.get(position).getSum();
                AboutUser.type_buyer = categories.get(position).getType_buyer();
                AboutUser.nomer_cheka = categories.get(position).getNomer_cheka();
                AboutUser.data_prodazhi = categories.get(position).getData_prodazhi();
            }
            else
                Toast.makeText(context, "Смена закрито!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView type_buyer;
        TextView viruchka;
        TextView sum;
        ImageButton info_icon;
        ProgressBar info_progress;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            type_buyer = itemView.findViewById(R.id.type_buyer);
            viruchka = itemView.findViewById(R.id.viruchka);
            sum = itemView.findViewById(R.id.sum);
            info_icon = itemView.findViewById(R.id.info_icon);
            info_progress = itemView.findViewById(R.id.info_progress);
        }
    }
}
