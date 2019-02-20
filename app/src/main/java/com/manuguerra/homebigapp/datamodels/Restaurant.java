package com.manuguerra.homebigapp.datamodels;

import com.manuguerra.homebigapp.ui.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Restaurant {

    public static final String ENDPOINT="restaurants/";
    private float minimo_ordine;
    private String nome_ristorante;
    private String indirizzo;
    private String image;
    private String id;

    public Restaurant(String nome_ristorante,String indirizzo,float minimo_ordine,String image,String id){
        this.nome_ristorante=nome_ristorante;
        this.indirizzo=indirizzo;
        this.minimo_ordine=minimo_ordine;
        this.image=image;
        this.id=id;
    }
    public Restaurant(JSONObject jsonRestaurant)throws JSONException{

            this.nome_ristorante=jsonRestaurant.getString("name");
            this.indirizzo=jsonRestaurant.getString("address");
            this.minimo_ordine=Float.valueOf(jsonRestaurant.getString("min_order"));
            this.image=jsonRestaurant.getString("image_url");
            this.id=jsonRestaurant.getString("id");
    }

    public float getMinimo_ordine() {
        return minimo_ordine;
    }

    public void setMinimo_ordine(float minimo_ordine) {
        this.minimo_ordine = minimo_ordine;
    }

    public String getNome_ristorante() {
        return nome_ristorante;
    }

    public void setNome_ristorante(String nome_ristorante) {
        this.nome_ristorante = nome_ristorante;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
