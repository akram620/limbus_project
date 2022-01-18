package com.akram.limbus.dialog;

import android.annotation.SuppressLint;

import com.akram.limbus.adapter.AdapterVozvrat;


public class Class_change_adapter_bottom_sheet_vozvrat {
    public static AdapterVozvrat adapterVozvrat;

    public Class_change_adapter_bottom_sheet_vozvrat(AdapterVozvrat adapterVozvrat) {
        this.adapterVozvrat = adapterVozvrat;
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void change_adapter_bottom_sheet_vozvrat(){
        adapterVozvrat.notifyDataSetChanged();
    }
}
