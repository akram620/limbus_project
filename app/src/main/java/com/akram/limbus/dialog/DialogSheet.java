package com.akram.limbus.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.akram.limbus.R;
import com.akram.limbus.db.TokenList;
import com.akram.limbus.adapter.AdapterSee;
import com.akram.limbus.adapter.AdapterVozvrat;
import com.akram.limbus.model.AboutUser;
import com.akram.limbus.model.CategorySee;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogSheet extends AppCompatActivity {

    public Context context;

    Class_change_adapter_bottom_sheet_vozvrat class_change_adapter_bottom_sheet_vozvrat;

    public DialogSheet(Context context) {
        this.context = context;
    }
    public TextView naimenavanie, address, fio, phone, prodazha_nomer, date, check_na_summu, type_buyer;
    Button vozvrat_btn, cancel_vozvrat, vozvrat_main;
    //    see recycler
    public RecyclerView recSeeThing;
    public RecyclerView recSeeThingVozvrat;

    public AdapterSee adapterSee;
    public AdapterVozvrat adapterVozvrat;
    public List<CategorySee> categorySeeList = new ArrayList<>();

//    layout of vozvrat
    RelativeLayout isMainListLay;
    LinearLayout layout_vozvrat;

    public ImageButton exit;


    public void addList(String id, String name, String quantity, String code, String articul, String total_price){
        categorySeeList.add(new CategorySee(id, name, quantity, code, articul, total_price));
    }

    @SuppressLint("SetTextI18n")
    public void sheet(Context context){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BaseBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.info_thing);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        RecyclerView.LayoutManager layoutManagerVozv = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        recSeeThing = bottomSheetDialog.findViewById(R.id.recSeeThing);
        recSeeThingVozvrat = bottomSheetDialog.findViewById(R.id.recSeeThingVozvrat);

//        title of list const
        naimenavanie = bottomSheetDialog.findViewById(R.id.naimenavanie);
        naimenavanie.setText(AboutUser.naimenovanie);
        address = bottomSheetDialog.findViewById(R.id.address);
        address.setText(AboutUser.adres);
        fio = bottomSheetDialog.findViewById(R.id.fio);
        fio.setText(AboutUser.name);
        prodazha_nomer = bottomSheetDialog.findViewById(R.id.prodazha_nomer);
        prodazha_nomer.setText(AboutUser.nomer_cheka);
        date = bottomSheetDialog.findViewById(R.id.date);
        date.setText(AboutUser.data_prodazhi);
        check_na_summu = bottomSheetDialog.findViewById(R.id.check_na_summu);
        check_na_summu.setText(AboutUser.check_na_summu);
        type_buyer = bottomSheetDialog.findViewById(R.id.type_buyer);
        type_buyer.setText(AboutUser.type_buyer + "");

//        layout visibility
        isMainListLay = bottomSheetDialog.findViewById(R.id.isMainListLay);
        isMainListLay.setVisibility(View.VISIBLE);

        layout_vozvrat = bottomSheetDialog.findViewById(R.id.layout_vozvrat);
        layout_vozvrat.setVisibility(View.GONE);

        vozvrat_btn = bottomSheetDialog.findViewById(R.id.vozvrat_btn);
        cancel_vozvrat = bottomSheetDialog.findViewById(R.id.cancel_vozvrat);
        vozvrat_main = bottomSheetDialog.findViewById(R.id.vozvrat_main);

        recSeeThing.setLayoutManager(layoutManager);
        recSeeThingVozvrat.setLayoutManager(layoutManagerVozv);

        adapterSee = new AdapterSee(context, categorySeeList);
        adapterVozvrat = new AdapterVozvrat(context, categorySeeList);
        class_change_adapter_bottom_sheet_vozvrat = new Class_change_adapter_bottom_sheet_vozvrat(adapterVozvrat);

        recSeeThing.setAdapter(adapterSee);
        recSeeThing.setVisibility(View.VISIBLE);

        recSeeThingVozvrat.setAdapter(adapterVozvrat);
        recSeeThingVozvrat.setVisibility(View.GONE);

        change_adapter_bottom_sheet_vozvrat(adapterVozvrat);

        vozvrat_btn.setOnClickListener(v -> {
//            AdapterVozvrat.categoryVozvratList.clear();
            isMainListLay.setVisibility(View.GONE);
            layout_vozvrat.setVisibility(View.VISIBLE);

            recSeeThing.setVisibility(View.GONE);
            recSeeThingVozvrat.setVisibility(View.VISIBLE);
        });
        cancel_vozvrat.setOnClickListener(v -> {
            isMainListLay.setVisibility(View.VISIBLE);
            layout_vozvrat.setVisibility(View.GONE);

            recSeeThing.setVisibility(View.VISIBLE);
            recSeeThingVozvrat.setVisibility(View.GONE);
        });

        vozvrat_main.setOnClickListener(v -> {
            for (int i = 0; i < AdapterVozvrat.categoryVozvratList.size(); i++){
                if (AdapterVozvrat.categoryVozvratList.get(i).isChecked())
                    Toast.makeText(context, "id = " + AdapterVozvrat.categoryVozvratList.get(i).getId() + "\nquantity = " + AdapterVozvrat.categoryVozvratList.get(i).getSelect_count() + "", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "NO DATA", Toast.LENGTH_SHORT).show();
            }
        });


        exit = bottomSheetDialog.findViewById(R.id.exit);
        exit.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void change_adapter_bottom_sheet_vozvrat(AdapterVozvrat adapterVozvrat) {
    }

    public void seeDefinite(Context context, String prodazha_id, ProgressBar info_progress, ImageButton info_icon){
        categorySeeList.clear();
        String url = "http://limbus.tj/api/v1/prodazha/" + prodazha_id + "/show";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                info_progress.setVisibility(View.GONE);
                info_icon.setVisibility(View.VISIBLE);
                try {
                    JSONObject object = new JSONObject(response);
                    String itogo = object.getString("itogo");
                    JSONArray tovary = object.getJSONArray("tovary");
                    Log.d("my", "tovary= " + tovary);
                    if (tovary.length() != 0){
                        for (int i = 0; i < tovary.length(); i++){
                            JSONObject tovar = tovary.getJSONObject(i);
                            String id = tovar.getString("id");
                            String naimenovanie = tovar.getString("naimenovanie");
                            String kolichestvo = tovar.getString("kolichestvo");
                            String kod_tovara = tovar.getString("kod_tovara");
                            String artikul = tovar.getString("artikul");

                            JSONObject edinica_izmerenija_obj = tovar.getJSONObject("edinica_izmerenija");
                            String edinica = edinica_izmerenija_obj.getString("naimenovanie");

                            String prodazhnaja_cena = tovar.getString("prodazhnaja_cena");
//                            kolichestvo = kolichestvo + " " + edinica;

                            addList(id, naimenovanie, kolichestvo, kod_tovara, artikul, prodazhnaja_cena);
                        }
                        sheet(context);
                    }

                } catch (JSONException e) {
                    Log.d("my", "err res = " + e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                info_progress.setVisibility(View.GONE);
                info_icon.setVisibility(View.VISIBLE);
                if (error.toString().trim().equals("com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host \"limbus.loihaho.info\": No address associated with hostname")){
//                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Проверьте ваше интернет-соединение!", Snackbar.LENGTH_SHORT);
//                    snackbar.show();
                    Toast.makeText(context, "Проверьте интернет-соединения!", Toast.LENGTH_SHORT).show();
                    Log.d("my", "internet");
                }
                else if(error.toString().trim().equals("com.android.volley.TimeoutError")){
//                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Попробуйте позже(Time out)!", Snackbar.LENGTH_SHORT);
//                    snackbar.show();
                    Toast.makeText(context, "Попробуйте позже(Time out)!", Toast.LENGTH_SHORT).show();
                }
                else if (error.toString().trim().equals("com.android.volley.AuthFailureError")) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject messagefill = new JSONObject(responseBody);
                        String message = messagefill.getString("message");

//                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message + "", Snackbar.LENGTH_SHORT);
//                        snackbar.show();
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
//                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e + "", Snackbar.LENGTH_SHORT);
//                        snackbar.show();
                        Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
//                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Unknown problem!", Snackbar.LENGTH_SHORT);
//                    snackbar.show();
                    Log.d("my", "Unknown");
                    Toast.makeText(context, "Unknown problem!", Toast.LENGTH_SHORT).show();
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

}
