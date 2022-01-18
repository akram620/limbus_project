package com.akram.limbus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.akram.limbus.R;
import com.akram.limbus.activity.oformit;
import com.akram.limbus.model.AboutUser;
import com.akram.limbus.usecase.AddItemInSale;

import java.util.List;

public class AdapterNewItems extends RecyclerView.Adapter<AdapterNewItems.NewItemsViewHolder> {

    Context context;
    List<AboutUser.CategoryNewItems> categoryNewItemsList;

    public AdapterNewItems(Context context, List<AboutUser.CategoryNewItems> categoryNewItemsList) {
        this.context = context;
        this.categoryNewItemsList = categoryNewItemsList;
    }

    @NonNull
    @Override
    public NewItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newItems = LayoutInflater.from(context).inflate(R.layout.choosed_item_recycler, parent, false);
        return new AdapterNewItems.NewItemsViewHolder(newItems);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewItemsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TextWatcher textWatcher = null;
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String qwen_str = holder.qwentity.getText().toString();
                if (qwen_str.trim().length() != 0){
                    if (Integer.parseInt(qwen_str) == 0){
                        categoryNewItemsList.get(position).setQwentity(1);
                        holder.qwentity.setText("1");
                        holder.total_price.setText(categoryNewItemsList.get(position).getPrice() + " руб");
                        oformit.change_all_summ();
                    }else {
                        categoryNewItemsList.get(position).setQwentity(Integer.parseInt(qwen_str));
                        double total = categoryNewItemsList.get(position).getPrice() * categoryNewItemsList.get(position).getQwentity();
                        holder.total_price.setText(total + " руб");
                        oformit.change_all_summ();
                    }
                }else{
                    categoryNewItemsList.get(position).setQwentity(1);
                    holder.qwentity.setText("1");
                    holder.total_price.setText(categoryNewItemsList.get(position).getPrice() + " руб");
                    oformit.change_all_summ();
                }
            }
        };
        holder.qwentity.addTextChangedListener(textWatcher);

        holder.number.setText(categoryNewItemsList.get(position).getNumber() + ".");
        holder.name.setText(categoryNewItemsList.get(position).getName());
        holder.qwentity.setText(categoryNewItemsList.get(position).getQwentity() + "");
        holder.price.setText(categoryNewItemsList.get(position).getPrice() + " руб");
        double total = categoryNewItemsList.get(position).getPrice() * categoryNewItemsList.get(position).getQwentity();
        holder.total_price.setText(total + " руб");


        holder.btn_delete.setOnClickListener(v -> {
            categoryNewItemsList.remove(position);
            oformit.datachange();
            if (AddItemInSale.getSize() == 0)
                oformit.add_items_no();
            else
                oformit.add_items_yes();

            oformit.change_all_summ();
            oformit.create_text();
        });

        holder.btn_minus.setOnClickListener(v -> {
            int q = categoryNewItemsList.get(position).getQwentity();
            if (q > 1){
                q--;
                categoryNewItemsList.get(position).setQwentity(q);
                holder.qwentity.setText(q + "");
            }
        });
        holder.btn_plus.setOnClickListener(v -> {
            int q = categoryNewItemsList.get(position).getQwentity() + 1;
            categoryNewItemsList.get(position).setQwentity(q);
            holder.qwentity.setText(q + "");
        });
    }

    @Override
    public int getItemCount() {
        return categoryNewItemsList.size();
    }

    public static final class NewItemsViewHolder extends RecyclerView.ViewHolder{

        TextView number, name, price, total_price;
        EditText qwentity;
        ImageButton btn_delete;
        Button btn_minus, btn_plus;

        public NewItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            qwentity = itemView.findViewById(R.id.qwentity);
            price = itemView.findViewById(R.id.price);
            total_price = itemView.findViewById(R.id.total_price);

            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);
        }
    }
}
