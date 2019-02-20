package com.manuguerra.homebigapp.datamodels;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    private String name;
    private int quantity=0;
    private float price;

    public Product(String name,int quantity,float price){
        this.name=name;
        this.quantity=quantity;
        this.price=price;
    }
    public Product(JSONObject jsonProduct)throws JSONException {

        this.name=jsonProduct.getString("name");
        this.price=Float.valueOf(jsonProduct.getString("price"));
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void increaseQuantity(){
        this.quantity++;
    }
    public void decreaseQuantity(){
        if(quantity<=0)return;
        else  this.quantity--;
    }

}
