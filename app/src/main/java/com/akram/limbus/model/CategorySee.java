package com.akram.limbus.model;

public class CategorySee {
    String id;
    String name;
    String quantity;
    String code;
    String articul;
    String total_price;

    public CategorySee(String id, String name, String quantity, String code, String articul, String total_price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.code = code;
        this.articul = articul;
        this.total_price = total_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArticul() {
        return articul;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
