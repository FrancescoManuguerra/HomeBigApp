package com.manuguerra.homebigapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.datamodels.Product;
import com.manuguerra.homebigapp.datamodels.Restaurant;
import com.manuguerra.homebigapp.interfaces.StartActivityInterface;

import java.util.ArrayList;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder>  {

    private ArrayList<Restaurant> dati;
    private boolean gridOn;
    private StartActivityInterface activityInterface;
    private Context context;


    public ModelAdapter(ArrayList<Restaurant> dati, StartActivityInterface activityInterface,boolean gridOn,Context context) {
        this.dati = dati;
        this.activityInterface=activityInterface;
        this.gridOn=gridOn;
        this.context=context;
    }
    public ModelAdapter(Context context){
        this.context=context;
        new ModelAdapter(new ArrayList<Restaurant>(),null, gridOn, context);
    }

    public void setData(ArrayList<Restaurant> dati){
        this.dati=dati;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(gridOn) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item2, viewGroup, false);
            return new ViewHolder(v);
        }else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
            return new ViewHolder(v);
        }
    }

    //tramite il view holder stta i dati dei componenti
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)  {
        final Restaurant dato = dati.get(i);
        viewHolder.name_restaurant.setText(dato.getNome_ristorante() );
        viewHolder.indirizzo_restaurant.setText(dato.getIndirizzo());
        viewHolder.ordine_minimo.setText("Ordine minimo: "+String.valueOf(dato.getMinimo_ordine())+" €");


        RequestManager requestManager = Glide.with(context);
        RequestBuilder requestBuilder = requestManager.load(dato.getImage());
        requestBuilder.into(viewHolder.imageView);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInterface.startShoptActivity(dato);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dati.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView name_restaurant;
        final TextView indirizzo_restaurant;
        final TextView ordine_minimo;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_restaurant = itemView.findViewById(R.id.name_restaurant_tv);
            indirizzo_restaurant=itemView.findViewById(R.id.indirizzo_restaurant_tv);
            ordine_minimo=itemView.findViewById(R.id.ordine_minimo_tv);
            imageView=itemView.findViewById(R.id.image_iv);

        }
    }
}

