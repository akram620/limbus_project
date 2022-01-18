package com.akram.limbus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akram.limbus.MainActivity;
import com.akram.limbus.R;
import com.akram.limbus.adapter.AdapterNewItems;
import com.akram.limbus.db.DBHelper;
import com.akram.limbus.db.TokenList;
import com.akram.limbus.model.AboutUser;
import com.akram.limbus.usecase.AddItemInSale;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class oformit extends AppCompatActivity {

    public static RecyclerView recycler_choose_items;
    @SuppressLint("StaticFieldLeak")
    public static AdapterNewItems adapterNewItems;

    @SuppressLint("StaticFieldLeak")
    public static LinearLayout add_items;
    ProgressDialog progressDialog;

    Button add, cancel, sale;
    ImageView cancel_1;
    EditText discount_edittext;
    static TextView total_ans, total_discount, total_exp, create_sale;
    public static double dis;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oformit);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.main_color));

        dbHelper = dbHelper = new DBHelper(oformit.this);

        add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            Intent i = new Intent(this, Add.class);
            startActivity(i);
            finish();
        });
        create_sale = findViewById(R.id.create_sale);
        create_text();

        cancel = findViewById(R.id.cancelOn);
        cancel_1 = findViewById(R.id.cancel_1);
        cancel.setOnClickListener(v -> {
            Intent i = new Intent(this, Main_page.class);
            startActivity(i);
            finish();
        });
        cancel_1.setOnClickListener(v -> {
            Intent i = new Intent(this, Main_page.class);
            startActivity(i);
            finish();
        });

        discount_edittext = findViewById(R.id.discount_edittext);

        dis = 0;

        total_ans = findViewById(R.id.total_ans);
        total_ans.setText(summ_start() + "c");
        total_discount = findViewById(R.id.total_discount);
        total_discount.setText(dis + "c");
        total_exp = findViewById(R.id.total_exp);
        total_exp.setText(summ_start() + "c");

        recycler_choose_items = findViewById(R.id.recycler_choose_items);
        add_items = findViewById(R.id.add_items);

        progressDialog = new ProgressDialog(this);

        TextWatcher textWatcher = null;
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!discount_edittext.getText().toString().trim().equals("")){
                    if (Double.parseDouble(discount_edittext.getText().toString().trim()) < 1000){
                        dis = Double.parseDouble(discount_edittext.getText().toString().trim());
                        total_exp.setText((summ_start() - dis) + "c");
                        total_discount.setText(dis + "c");
                    }else {
                        dis = 999;
                        total_exp.setText((summ_start() - dis) + "c");
                        discount_edittext.setText("999");
                        total_discount.setText(dis + "c");
                    }
                }else{
                    dis = 0;
                    total_exp.setText(summ_start() + "c");
                    total_discount.setText("0.0c");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        discount_edittext.addTextChangedListener(textWatcher);

        setCategoryRecycler(AddItemInSale.returnList());

        if (AddItemInSale.getSize() == 0)
            add_items_no();
        else
            add_items_yes();

        sale = findViewById(R.id.sale);
        sale.setOnClickListener(v -> {
            create1();
        });
    }
    private void setCategoryRecycler(List<AboutUser.CategoryNewItems> categoryNewItemsList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recycler_choose_items.setLayoutManager(layoutManager);

        adapterNewItems = new AdapterNewItems(this, categoryNewItemsList);
        recycler_choose_items.setAdapter(adapterNewItems);
    }

    public static void datachange(){
        adapterNewItems.notifyDataSetChanged();
    }

    public static double summ_start(){
        int len = AddItemInSale.getSize();
        double sum_st = 0;
        if (len != 0){
            for (int j = 0; j < len; j++){
                sum_st += AddItemInSale.returnList().get(j).getPrice() * AddItemInSale.returnList().get(j).getQwentity();
            }
        }
        return sum_st;
    }

    public static void change_all_summ(){
        total_ans.setText(summ_start() + "c");
        total_exp.setText((summ_start() - dis) + "c");
    }

    public static void create_text(){
        create_sale.setText("Количество товаров [" + AddItemInSale.getSize() + "]");
    }

    public static void add_items_no(){
        add_items.setVisibility(View.VISIBLE);
        recycler_choose_items.setVisibility(View.GONE);
    }

    public static void add_items_yes(){
        add_items.setVisibility(View.GONE);
        recycler_choose_items.setVisibility(View.VISIBLE);
    }

    public void create1(){
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_view);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);

        Log.d("my", "create");
        JSONArray tovaryArray = new JSONArray();

        for (int i = 0; i < AddItemInSale.returnList().size(); i++){
            JSONObject jsonObjectTovar = new JSONObject();
            try {
                jsonObjectTovar.put("tovar_id", AddItemInSale.returnList().get(i).getId());
                jsonObjectTovar.put("kolichestvo", AddItemInSale.returnList().get(i).getQwentity());
                jsonObjectTovar.put("itogo", AddItemInSale.returnList().get(i).getPrice() *
                        AddItemInSale.returnList().get(i).getQwentity());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(oformit.this, "1" + e.toString(), Toast.LENGTH_SHORT).show();
            }
            tovaryArray.put(jsonObjectTovar);
        }

        String url = "http://limbus.tj/api/v1/prodazha/store";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject object = new JSONObject();

        String skidka = discount_edittext.getText().toString().trim();
        if (skidka.equals("")){
            skidka = "0";
        }
        try {
            object.put("tovary", tovaryArray);
            object.put("v_roznicu", true);
            object.put("prochaja_skidka", skidka);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    String success = response.getString("success");
                    Toast.makeText(oformit.this, "" + success, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(oformit.this, Main_page.class);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(oformit.this, "3" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error.toString().trim().equals("com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host \"limbus.loihaho.info\": No address associated with hostname")){
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Проверьте ваше интернет-соединение!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if(error.toString().trim().equals("com.android.volley.TimeoutError")){
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Попробуйте позже(Time out)!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else if (error.toString().trim().equals("com.android.volley.AuthFailureError")) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject messagefill = new JSONObject(responseBody);
                        String message = messagefill.getString("message");

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message + "", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e + "", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    Intent i = new Intent(oformit.this, MainActivity.class);
                    dbHelper.deletData();
                    TokenList.token_list.clear();
                    startActivity(i);
                    finish();
                }
                else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Unknown problem!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        })

        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + TokenList.token_list.get(TokenList.token_list.size() - 1));
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
}