package com.akram.limbus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


import com.akram.limbus.activity.Main_page;
import com.akram.limbus.db.TokenList;
import com.akram.limbus.db.DBHelper;
import com.akram.limbus.model.AboutUser;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button login_btn, scann;
    EditText login_ed, password_ed;
    ProgressDialog progressDialog;
    DBHelper dbHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.main_color));

        dbHelper = new DBHelper(MainActivity.this);

        login_ed = findViewById(R.id.login);
        password_ed = findViewById(R.id.password);
//        Log.d("my", "st");
//        Log.d("my", "conn - " + isNetworkConnected());

        if (isNetworkConnected()){
            checkToken();
            int s = TokenList.token_list.size();
            if (s != 0){
                Intent i = new Intent(MainActivity.this, Main_page.class);
                startActivity(i);
                finish();
            }
        }

        progressDialog = new ProgressDialog(MainActivity.this);
        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(v ->{
            String login = login_ed.getText().toString();
            String password = password_ed.getText().toString();

            if (login.toString().trim().equals("")) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Введите логин!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            else if (password.toString().equals("")){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Введите пароль!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            else {
                authorization(login, password);
            }
        });
    }

    public void authorization(String login, String password){
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_view);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);

        String url = "http://limbus.tj/api/login";
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject object = new JSONObject();
        try {
            object.put("login", login);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString("token");

                    JSONObject user = response.getJSONObject("user");
                    String name = user.getString("name");
                    String telefon = user.getString("telefon");

                    JSONObject torgovaja_tochka = user.getJSONObject("torgovaja_tochka");
                    String naimenovanie = torgovaja_tochka.getString("naimenovanie");
                    String adres = torgovaja_tochka.getString("adres");

                    JSONObject can = user.getJSONObject("can");
                    String otkryt_smenu = can.getString("zakryt_smenu");

                    boolean result = dbHelper.insertData(token, naimenovanie, adres, name, telefon);
                    TokenList.token_list.add(token);
                    if (result){
                        Log.d("my", "token inserted");
                    }else {
                        Log.d("my", "token didn't insert");
                    }

                    AboutUser.name = name;
                    AboutUser.telefon = telefon;
                    AboutUser.naimenovanie = naimenovanie;
                    AboutUser.adres = adres;
                    AboutUser.otkryt_smenu = Boolean.parseBoolean(otkryt_smenu);
                    AboutUser.token = token;

                    progressDialog.dismiss();

                    Intent i = new Intent(MainActivity.this, Main_page.class);
                    startActivity(i);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e + "!", Snackbar.LENGTH_SHORT);
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
                }else if(error.toString().trim().equals("com.android.volley.TimeoutError")){
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Попробуйте позже(Time out)!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else if (error.toString().trim().equals("com.android.volley.ClientError")) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject message = new JSONObject(responseBody);
                        String login = message.getJSONObject("errors").getJSONArray("login").getString(0);
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), login + "!", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.parseColor("#FF4A38"));
                        snackbar.show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e + "!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Unknown problem!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        })
        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    public void checkToken(){
        Cursor res = dbHelper.getAllData();
        if (res != null && res.getCount() > 0){
            while (res.moveToNext()){
                TokenList.token_list.add(res.getString(0));
                AboutUser.naimenovanie = res.getString(1);
                AboutUser.adres = res.getString(2);
                AboutUser.name = res.getString(3);
                AboutUser.telefon = res.getString(4);
            }
        }else {
            Log.d("my", "not data");
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}