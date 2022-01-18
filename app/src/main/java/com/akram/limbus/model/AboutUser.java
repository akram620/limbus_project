package com.akram.limbus.model;

public class AboutUser {
    public static String name = "null";
    public static String telefon = "null";
    public static String naimenovanie = "null";
    public static String adres = "null";
    public static boolean otkryt_smenu;
    public static String token = "";

    public static String check_na_summu = "null";
    public static String type_buyer = "null";

    public static String nomer_cheka = "null";
    public static String data_prodazhi = "null";

    public static class CategoryNewItems {
        String id;
        int number;
        String name;
        int qwentity;
        double price;

        public CategoryNewItems(String id, int number, String name, int qwentity, double price) {
            this.id = id;
            this.number = number;
            this.name = name;
            this.qwentity = qwentity;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQwentity() {
            return qwentity;
        }

        public void setQwentity(int qwentity) {
            this.qwentity = qwentity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}