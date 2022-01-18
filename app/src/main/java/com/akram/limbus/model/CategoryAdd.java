package com.akram.limbus.model;

public class CategoryAdd {
    String id;
    String name;
    int price;
    String quantity;
    String code;
    String vendor_code;
    String barcode;

    public CategoryAdd(String id, String name, int price, String quantity, String code, String vendor_code, String barcode) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.code = code;
        this.vendor_code = vendor_code;
        this.barcode = barcode;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getVendor_code() {
        return vendor_code;
    }

    public void setVendor_code(String vendor_code) {
        this.vendor_code = vendor_code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
