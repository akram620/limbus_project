package com.akram.limbus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.akram.limbus.R;
import com.akram.limbus.activity.oformit;
import com.akram.limbus.model.CategoryAdd;
import com.akram.limbus.usecase.AddItemInSale;

import java.util.List;

public class AdapterAdd extends RecyclerView.Adapter<AdapterAdd.AddViewHolder>{

    Context context;
    List<CategoryAdd> categoryAddList;

    public AdapterAdd(Context context, List<CategoryAdd> categoryAddList) {
        this.context = context;
        this.categoryAddList = categoryAddList;
    }

    @NonNull
    @Override
    public AddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newItems = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        return new AdapterAdd.AddViewHolder(newItems);
    }

    @Override
    public void onBindViewHolder(@NonNull AddViewHolder holder, int position) {
        holder.name.setText(categoryAddList.get(position).getName());
        holder.price.setText(categoryAddList.get(position).getPrice() + " c");
        holder.quantity.setText(categoryAddList.get(position).getQuantity());
        holder.code.setText(categoryAddList.get(position).getCode());
        holder.vendor_code.setText(categoryAddList.get(position).getVendor_code());
        holder.barcode.setText(categoryAddList.get(position).getBarcode());

        holder.addsale.setOnClickListener(v -> {
            String id = categoryAddList.get(position).getId();
            boolean resb = false;
            int res = 0;

            for (int i = 0; i < AddItemInSale.returnList().size(); i++){
                if (id.equals(AddItemInSale.returnList().get(i).getId())){
                    resb = true;
                    res = i;
                    break;
                }
            }
            if (resb){
                int q = AddItemInSale.returnList().get(res).getQwentity();
                q++;
                AddItemInSale.returnList().get(res).setQwentity(q);
                oformit.datachange();
                Toast.makeText(context, "Добавлен " + q, Toast.LENGTH_SHORT).show();
            }else {
                AddItemInSale.additemsalefunc(categoryAddList.get(position).getId(),
                        AddItemInSale.getSize() + 1,
                        categoryAddList.get(position).getName(),
                        1,
                        categoryAddList.get(position).getPrice());
                Toast.makeText(context, "Добавлен 1", Toast.LENGTH_SHORT).show();
            }
            });
    }

    @Override
    public int getItemCount() {
        return categoryAddList.size();
    }


    public static final class AddViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, quantity, code, vendor_code, barcode;
        ImageButton addsale;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            code = itemView.findViewById(R.id.code);
            vendor_code = itemView.findViewById(R.id.vendor_code);
            barcode = itemView.findViewById(R.id.barcode);

            addsale = itemView.findViewById(R.id.addsale);
        }
    }
}
