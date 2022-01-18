package com.akram.limbus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akram.limbus.MainActivity;
import com.akram.limbus.R;
import com.akram.limbus.db.TokenList;
import com.akram.limbus.adapter.AdapterAdd;
import com.akram.limbus.db.DBHelper;
import com.akram.limbus.model.CategoryAdd;
import com.akram.limbus.scanner.ActivityScanner;
import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add extends AppCompatActivity {

    CategoryAdd categoryAdd;
    RecyclerView recadd;
    LinearLayout not_found;
    ProgressBar info_progress;

    Button exit;
    ImageView exit1;
    ImageButton find, bear_scanner;

    EditText number_reciept;
    private TextWatcher textWatcher = null;

    DBHelper dbHelper;

    static AdapterAdd adapterAdd;
    static List<CategoryAdd> categoryAddList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.main_color));


        recadd = findViewById(R.id.recadd);
        not_found = findViewById(R.id.not_found);
        info_progress = findViewById(R.id.info_progress);



//        search
        number_reciept = findViewById(R.id.number_reciept);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = number_reciept.getText().toString().trim();
                if (query.length() >= 3){
                    search_item(query);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        number_reciept.addTextChangedListener(textWatcher);

        bear_scanner = findViewById(R.id.bear_scanner);
        bear_scanner.setOnClickListener(v -> {
            scann_code();
        });

        find = findViewById(R.id.find);
        find.setOnClickListener(v -> {
            String quer = number_reciept.getText().toString().trim();
            search_item(quer);
        });


        exit = findViewById(R.id.exit);
        exit.setOnClickListener(v -> {
            exitPage();
        });

        exit1 = findViewById(R.id.exit1);
        exit1.setOnClickListener(v -> {
            exitPage();
        });

        if (ActivityScanner.result_of_scanner != null){
            search_item(ActivityScanner.result_of_scanner);
        }

    }

    private void setCategoryRecycler(List<CategoryAdd> categoryAddList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recadd.setLayoutManager(layoutManager);

        adapterAdd = new AdapterAdd(this, categoryAddList);
        recadd.setAdapter(adapterAdd);
    }

    private void exitPage(){
        Intent i = new Intent(this, oformit.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        exitPage();
    }

    public void search_item(String query){
        info_progress.setVisibility(View.VISIBLE);
        find.setVisibility(View.GONE);

        categoryAddList.clear();

        String url = "http://limbus.tj/api/v1/poisk-tovarov?q=" + query;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                info_progress.setVisibility(View.GONE);
                find.setVisibility(View.VISIBLE);
                Log.d("my", "response = " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");

                    if (data.length() == 0){
                        not_found.setVisibility(View.VISIBLE);
                        recadd.setVisibility(View.GONE);
                    }else {
                        not_found.setVisibility(View.GONE);
                        recadd.setVisibility(View.VISIBLE);
                        for (int i = 0; i < data.length(); i++){
                            JSONObject tovari = data.getJSONObject(i);

                            JSONObject edinica_izmerenija = tovari.getJSONObject("edinica_izmerenija");
                            String edinica = edinica_izmerenija.getString("naimenovanie");

                            String id = tovari.getString("id");
                            String naimenovanie = tovari.getString("naimenovanie");
                            String prodazhnaja_cena	 = tovari.getString("prodazhnaja_cena");
                            int price = 0;

                            if (prodazhnaja_cena.equals("null")){
                                price = 0;
                            }else{
                                price = Integer.parseInt(prodazhnaja_cena);
                            }

                            String quantity	 = tovari.getString("kolichestvo");
                            quantity = quantity + " " + edinica;
                            String kod_tovara	 = tovari.getString("kod_tovara");
                            String artikul	 = tovari.getString("artikul");
                            String barcode	 = tovari.getString("shtrih_kod_tovara");

                            categoryAddList.add(new CategoryAdd(id, naimenovanie, price, quantity, kod_tovara, artikul, barcode));
                        }
                        setCategoryRecycler(categoryAddList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                info_progress.setVisibility(View.GONE);
                find.setVisibility(View.VISIBLE);

                not_found.setVisibility(View.VISIBLE);
                recadd.setVisibility(View.GONE);
                Log.d("my", "error = " + error.toString());
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

                    Intent i = new Intent(Add.this, MainActivity.class);
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

    private void scann_code(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, ActivityScanner.class);
            startActivity(intent);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

}