package com.akram.limbus.model;

public class CategoryVozvrat {
    String id;
    int select_count;
    int total_count;
    boolean isChecked;

    public CategoryVozvrat(String id, int select_count, int total_count, boolean isChecked) {
        this.id = id;
        this.select_count = select_count;
        this.total_count = total_count;
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSelect_count() {
        return select_count;
    }

    public void setSelect_count(int select_count) {
        this.select_count = select_count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}