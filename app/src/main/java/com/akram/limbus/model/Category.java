package com.akram.limbus.model;

public class Category {
    String id;
    String type_buyer;
    String viruchka;
    String sum;
    String nomer_cheka;
    String data_prodazhi;

    public Category(String id, String type_buyer, String viruchka, String sum, String nomer_cheka, String data_prodazhi) {
        this.id = id;
        this.type_buyer = type_buyer;
        this.viruchka = viruchka;
        this.sum = sum;
        this.nomer_cheka = nomer_cheka;
        this.data_prodazhi = data_prodazhi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_buyer() {
        return type_buyer;
    }

    public void setType_buyer(String type_buyer) {
        this.type_buyer = type_buyer;
    }

    public String getViruchka() {
        return viruchka;
    }

    public void setViruchka(String viruchka) {
        this.viruchka = viruchka;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getNomer_cheka() {
        return nomer_cheka;
    }

    public void setNomer_cheka(String nomer_cheka) {
        this.nomer_cheka = nomer_cheka;
    }

    public String getData_prodazhi() {
        return data_prodazhi;
    }

    public void setData_prodazhi(String data_prodazhi) {
        this.data_prodazhi = data_prodazhi;
    }
}
