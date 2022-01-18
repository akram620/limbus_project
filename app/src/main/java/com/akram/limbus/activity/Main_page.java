package com.akram.limbus.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.akram.limbus.MainActivity;
import com.akram.limbus.R;
import com.akram.limbus.usecase.AddItemInSale;
import com.akram.limbus.usecase.Smena;
import com.akram.limbus.db.TokenList;
import com.akram.limbus.adapter.CategoryAdapter;
import com.akram.limbus.db.DBHelper;
import com.akram.limbus.model.AboutUser;
import com.akram.limbus.model.Category;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main_page extends AppCompatActivity {
    SwitchCompat turn;
    RelativeLayout main_relative;
    ImageButton find, clear;
    ImageView logout;
    EditText number_reciept;
    TextView result_time;
    TextView naimenavanie, address, fio, phone;
    TextView kolichestvo_checkov, total_sum_in_check_text;
    LinearLayout no_data;
    Button from_data, to_data, create_pro;
    double total_sum_in_check = 0;

    ProgressBar progress_bar_main_page;

    DBHelper dbHelper;

//    page
    String first = "null", last = "null", prev = "null", next = "null";
    LinearLayout page_layout;
    ImageButton first_b, last_b, prev_b, next_b;

//    exit dialog
    Button ok, cancel;
    AlertDialog dialog;

//    history recycler
    RecyclerView recycler_history;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList = new ArrayList<>();
//    progress dialog
    ProgressDialog progressDialog;


    @SuppressLint({"SetTextI18n", "ObsoleteSdkInt"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Log.d("my", "main page");

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.main_color));

//        progress
        progressDialog = new ProgressDialog(Main_page.this);
        progress_bar_main_page = findViewById(R.id.progress_bar_main_page);
        progress_bar_main_page.setVisibility(View.GONE);

//        recycler
        recycler_history = findViewById(R.id.recycler_history);
        recycler_history.setVisibility(View.VISIBLE);

//        database
        dbHelper = new DBHelper(Main_page.this);

//        button of list - page
        page_layout = findViewById(R.id.page_layout);
        page_layout.setVisibility(View.GONE);
        first_b = findViewById(R.id.first);
        last_b = findViewById(R.id.last);
        prev_b = findViewById(R.id.prev);
        next_b = findViewById(R.id.next);

//        for logout
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            AlertDialog.Builder alert;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }
            else {
                alert = new AlertDialog.Builder(this);
            }
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.data, null);

            ok = view.findViewById(R.id.okOn);
            cancel = view.findViewById(R.id.cancelOn);
            alert.setView(view);

            dialog = alert.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);

            cancel.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
            ok.setOnClickListener(view1 -> {
                dialog.dismiss();
                logout();
            });
        });

//        user
        naimenavanie = findViewById(R.id.naimenavanie);
        naimenavanie.setText(AboutUser.naimenovanie);
        address = findViewById(R.id.address);
        address.setText(AboutUser.adres);
        fio = findViewById(R.id.fio);
        fio.setText(AboutUser.name);
        phone = findViewById(R.id.phone);
        phone.setText(AboutUser.telefon);
        kolichestvo_checkov = findViewById(R.id.kolichestvo_checkov);
        total_sum_in_check_text = findViewById(R.id.total_sum_in_check_text);

//        no data in main page
        no_data = findViewById(R.id.no_data);
//        main page recycler
        recycler_history = findViewById(R.id.recycler_history);

//        smena
        turn = findViewById(R.id.turn);
        number_reciept = findViewById(R.id.number_reciept);

//        Date from
        from_data = findViewById(R.id.from_data);
        from_data.setText(now());
        from_data.setOnClickListener(v -> {
            showPickerDialog1(from_data);
        });
//        Date to
        to_data = findViewById(R.id.to_data);
        to_data.setText(now());
        to_data.setOnClickListener(v -> {
            showPickerDialog1(to_data);
        });
        result_time = findViewById(R.id.result_time);
        result_time.setText(now() + " - " + now());

//        Find
        find = findViewById(R.id.find);
        find.setOnClickListener(v -> {
            listSee(number_reciept.getText().toString(),
                    from_data.getText().toString(),
                    to_data.getText().toString());
            result_time.setText(from_data.getText().toString() +  " - " + to_data.getText().toString());
        });

//        Clear
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(v -> {
            number_reciept.setText("");
        });
//       get list
        listSee("", now(), now());

//        Smena switch
        main_relative = findViewById(R.id.main_relative);

//        smena implementation
        turn.setChecked(AboutUser.otkryt_smenu);
        Smena.setCheck(AboutUser.otkryt_smenu);
        disableEnableControls(AboutUser.otkryt_smenu, main_relative);
        turn.setOnClickListener(view -> {
            if (!turn.isChecked()){
                turn.setChecked(true);
                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                }
                else {
                    alert = new AlertDialog.Builder(this);
                }
                LayoutInflater inflater = getLayoutInflater();
                View view1 = inflater.inflate(R.layout.off_switch, null);

                ok = view1.findViewById(R.id.okOn);
                cancel = view1.findViewById(R.id.cancelOn);
                alert.setView(view1);

                dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);

                cancel.setOnClickListener(view2 -> {
                    dialog.dismiss();
                });
                ok.setOnClickListener(view2 -> {
                    dialog.dismiss();
                    smenaZakrit();
                });
            }
            else{
                turn.setChecked(false);
                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                }
                else {
                    alert = new AlertDialog.Builder(this);
                }
                LayoutInflater inflater = getLayoutInflater();
                View view5 = inflater.inflate(R.layout.on_switch, null);

                ok = view5.findViewById(R.id.okOn);
                cancel = view5.findViewById(R.id.cancelOn);
                alert.setView(view5);

                dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);

                cancel.setOnClickListener(view2 -> {
                    dialog.dismiss();
                });
                ok.setOnClickListener(view2 -> {
                    dialog.dismiss();
                    smenaOtk();
                });
            }
        });
//        create_prodaja
        create_pro = findViewById(R.id.create_pro);
        create_pro.setOnClickListener(v -> {
            Intent i = new Intent(this, oformit.class);
            AddItemInSale.returnList().clear();
            startActivity(i);
        });

//        page
        first_b.setOnClickListener(v -> {
            if (!first.equals("null")){
                pageSee(first);
            }
            else{
                Toast.makeText(Main_page.this, "Нет страница!", Toast.LENGTH_SHORT).show();
            }
        });

        last_b.setOnClickListener(v -> {
            if (!last.equals("null")){
                pageSee(last);
            }
            else{
                Toast.makeText(Main_page.this, "Нет страница!", Toast.LENGTH_SHORT).show();
            }
        });

        prev_b.setOnClickListener(v -> {
            if (!prev.equals("null")){
                pageSee(prev);
            }
            else{
                Toast.makeText(Main_page.this, "Нет страница!", Toast.LENGTH_SHORT).show();
            }
        });

        next_b.setOnClickListener(v -> {
            if (!next.equals("null")){
                pageSee(next);
            }
            else{
                Toast.makeText(Main_page.this, "Нет страница!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setCategoryRecycler(List<Category> categoryList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recycler_history.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList);
        recycler_history.setAdapter(categoryAdapter);
        
    }
    private void showPickerDialog1(Button from_data) {
        Calendar calendar  = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1++;
                String month = "" + i1;
                String day = "" + i2;
                if (month.length() == 1)
                    month = "0" + month;

                if (day.length() == 1)
                    day = "0" + day;
                String res = i + "-" + month + "-" + day;
                from_data.setText(res);
            }
        };
        new DatePickerDialog(Main_page.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }
    private String now(){
        GregorianCalendar gcalendar = new GregorianCalendar();
        int day = gcalendar.get(Calendar.DAY_OF_MONTH);
        int month = gcalendar.get(Calendar.MONTH) + 1;
        int year = gcalendar.get(Calendar.YEAR);
        String month_str = "" + month;
        String day_str = "" + day;
        if (month_str.length() == 1)
            month_str = "0" + month_str;
        if (day_str.length() == 1)
            day_str = "0" + day_str;
        String res = year + "-" + month_str + "-" + day_str;
        return res;
    }

    public void smenaOtk(){
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_view);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);

        String url = "http://limbus.tj/api/v1/smeny/otkryt";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject messagefill = new JSONObject(response);
                    String success = messagefill.getString("success");
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + success, Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.parseColor("#34c759"));
                    snackbar.show();

                    AboutUser.otkryt_smenu = true;
                    disableEnableControls(true, main_relative);
                    Smena.setCheck(true);
                    turn.setChecked(true);

                } catch (JSONException e) {
                    turn.setChecked(false);
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + e, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                turn.setChecked(false);
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
                    Intent i = new Intent(Main_page.this, MainActivity.class);
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
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + TokenList.token_list.get(TokenList.token_list.size() - 1));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    public void smenaZakrit(){
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_view);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);

        String url = "http://limbus.tj/api/v1/smeny/zakryt";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject messagefill = new JSONObject(response);
                    String success = messagefill.getString("success");
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), success + "!", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.parseColor("#34c759"));
                    snackbar.show();

                    AboutUser.otkryt_smenu = false;
                    disableEnableControls(false, main_relative);
                    Smena.setCheck(false);
                    turn.setChecked(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    turn.setChecked(true);

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + e, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                turn.setChecked(true);
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
                    Intent i = new Intent(Main_page.this, MainActivity.class);
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
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + TokenList.token_list.get(TokenList.token_list.size() - 1));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void listSee(String nomer_cheka, String nachalo_perioda, String okonchanie_perioda){
//        progressDialog.show();
//        progressDialog.setContentView(R.layout.progress_dialog_view);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progressDialog.setCanceledOnTouchOutside(false);
        recycler_history.setVisibility(View.GONE);
        page_layout.setVisibility(View.GONE);
        no_data.setVisibility(View.GONE);
        progress_bar_main_page.setVisibility(View.VISIBLE);


        String url = "http://limbus.tj/api/v1/prodazha?nomer_cheka=" + nomer_cheka + "&nachalo_perioda=" + nachalo_perioda + "&okonchanie_perioda=" + okonchanie_perioda;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("my", "list = " + response);
//                progressDialog.dismiss();
                progress_bar_main_page.setVisibility(View.GONE);

                categoryList.clear();
                total_sum_in_check = 0;
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONObject("prodazhi").getJSONArray("data");
                    JSONObject links = object.getJSONObject("prodazhi").getJSONObject("links");
                    String can = object.getJSONObject("can").getString("prodazha");
                    Log.d("my", "cannnnn" + can);
                    AboutUser.otkryt_smenu = Boolean.parseBoolean(can);

                    turn.setChecked(AboutUser.otkryt_smenu);
                    Smena.setCheck(AboutUser.otkryt_smenu);
                    disableEnableControls(AboutUser.otkryt_smenu, main_relative);


                    Log.d("my", "cannnnn" +  AboutUser.otkryt_smenu);
                    first = links.getString("first");
                    last = links.getString("last");
                    prev = links.getString("prev");
                    next = links.getString("next");

                    if (data.length() != 0){
                        no_data.setVisibility(View.GONE);
                        recycler_history.setVisibility(View.VISIBLE);
                        page_layout.setVisibility(View.VISIBLE);
                        for(int i = 0; i < data.length(); i++){
                            String id = data.getJSONObject(i).getString("id");
                            String nomer_cheka = data.getJSONObject(i).getString("nomer_cheka");
                            String data_prodazhi = data.getJSONObject(i).getString("data_prodazhi");
                            String v_roznicu = data.getJSONObject(i).getString("v_roznicu");
                            String type_buyer = null;
                            if (v_roznicu.toString().trim().equals("1")){
                                type_buyer = "Розничный покупатель";
                            }else {
                                type_buyer = "Контрагент";
                            }
                            String itogo = data.getJSONObject(i).getString("itogo");

                            total_sum_in_check += Double.parseDouble(itogo);

                            categoryList.add(new Category(id, type_buyer, "Выручка:", itogo + "c", nomer_cheka, data_prodazhi));
                        }
                        setCategoryRecycler(categoryList);
                        kolichestvo_checkov.setText(categoryAdapter.getItemCount() + "");
                        total_sum_in_check_text.setText(total_sum_in_check + "");
                    }else {
                        recycler_history.setVisibility(View.GONE);
                        page_layout.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                        kolichestvo_checkov.setText("0");
                        total_sum_in_check_text.setText("0c");
                    }
                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e + "", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                progress_bar_main_page.setVisibility(View.GONE);

                kolichestvo_checkov.setText("0");

                recycler_history.setVisibility(View.GONE);
                page_layout.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
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
                        Toast.makeText(Main_page.this, "" + message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Main_page.this, MainActivity.class);

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Main_page.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(Main_page.this, MainActivity.class);
                    dbHelper.deletData();
                    TokenList.token_list.clear();
                    startActivity(i);
                    finish();
                }
                else {
                    Log.d("my", "err - " + error);
                    Toast.makeText(Main_page.this, "Unknown" + error, Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Unknown problem - " + error, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + TokenList.token_list.get(TokenList.token_list.size() - 1));
                return params;
            }
        };
        queue.add(request);
    }

    public void pageSee(String url_page){
        recycler_history.setVisibility(View.GONE);
        page_layout.setVisibility(View.GONE);
        no_data.setVisibility(View.GONE);
        progress_bar_main_page.setVisibility(View.VISIBLE);

        String url = url_page;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progress_bar_main_page.setVisibility(View.GONE);

                categoryList.clear();
                total_sum_in_check = 0;
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONObject("prodazhi").getJSONArray("data");
                    JSONObject links = object.getJSONObject("prodazhi").getJSONObject("links");
                    first = links.getString("first");
                    last = links.getString("last");
                    prev = links.getString("prev");
                    next = links.getString("next");

                    if (data.length() != 0){
                        no_data.setVisibility(View.GONE);
                        recycler_history.setVisibility(View.VISIBLE);
                        page_layout.setVisibility(View.VISIBLE);
                        for(int i = 0; i < data.length(); i++){
                            String id = data.getJSONObject(i).getString("id");
                            String nomer_cheka = data.getJSONObject(i).getString("nomer_cheka");
                            String data_prodazhi = data.getJSONObject(i).getString("data_prodazhi");
                            String v_roznicu = data.getJSONObject(i).getString("v_roznicu");
                            String type_buyer = null;
                            if (v_roznicu.toString().trim().equals("1")){
                                type_buyer = "Розничный покупатель";
                            }else {
                                type_buyer = "Контрагент";
                            }
                            String itogo = data.getJSONObject(i).getString("itogo");

                            total_sum_in_check += Double.parseDouble(itogo);

                            categoryList.add(new Category(id, type_buyer, "Выручка:", itogo + "c", nomer_cheka, data_prodazhi));
                        }
                        setCategoryRecycler(categoryList);
                        kolichestvo_checkov.setText(categoryAdapter.getItemCount() + "");
                        total_sum_in_check_text.setText(total_sum_in_check + "");
                    }else {
                        recycler_history.setVisibility(View.GONE);
                        page_layout.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                        kolichestvo_checkov.setText("0");
                        total_sum_in_check_text.setText("0c");
                    }
                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e + "", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress_bar_main_page.setVisibility(View.GONE);

                kolichestvo_checkov.setText("0");

                recycler_history.setVisibility(View.GONE);
                page_layout.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);

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
                        Toast.makeText(Main_page.this, "" + message, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Main_page.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(Main_page.this, MainActivity.class);
                    dbHelper.deletData();
                    TokenList.token_list.clear();
                    startActivity(i);
                    finish();
                }
                else {
                    Log.d("my", "err - " + error);
                    Toast.makeText(Main_page.this, "Unknown" + error, Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Unknown problem - " + error, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + TokenList.token_list.get(TokenList.token_list.size() - 1));
                return params;
            }
        };
        queue.add(request);
    }
    public void logout(){
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_view);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
        String url = "http://limbus.tj/api/logout";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject messagefill = new JSONObject(response);
                    String message = messagefill.getString("message");
                    Toast.makeText(Main_page.this, "" + message, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Main_page.this, MainActivity.class);
                    dbHelper.deletData();
                    TokenList.token_list.clear();
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + e, Snackbar.LENGTH_SHORT);
                    snackbar.show();
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
                    Intent i = new Intent(Main_page.this, MainActivity.class);
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
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + TokenList.token_list.get(TokenList.token_list.size() - 1));
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

}