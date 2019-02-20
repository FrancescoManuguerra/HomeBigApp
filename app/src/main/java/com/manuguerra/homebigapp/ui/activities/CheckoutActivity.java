package com.manuguerra.homebigapp.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manuguerra.homebigapp.ui.adapter.CheckoutAdapter;
import com.manuguerra.homebigapp.datamodels.Product;
import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.interfaces.onTotalChangeListener;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity implements onTotalChangeListener {

    private RecyclerView recyclerView;
    private CheckoutAdapter productAdapter;
    private TextView checkout_total;
    private ArrayList <Product> products_to_pay=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_relative);



        //setproducts
        setProducts_to_pay();


        //setComponent
        checkout_total=findViewById(R.id.checkout_total_tv);
        recyclerView=findViewById(R.id.recycler_view_rv);

        //Set total
        setTotal(products_to_pay);
        //
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //error here
        productAdapter = new CheckoutAdapter(products_to_pay, this,this);
        recyclerView.setAdapter(productAdapter);

    }

    private void setTotal(ArrayList<Product> products_to_pay) {
        float total=0f;
        for(Product p: products_to_pay){
            total+=p.getPrice()*p.getQuantity();
        }
        checkout_total.setText(String.valueOf(total));
    }

    private void setProducts_to_pay(){

        products_to_pay.add(new Product("Primo prodotto",1,5F));
        products_to_pay.add(new Product("Secondo prodotto",3,6F));
        products_to_pay.add(new Product("Terzo prodotto",2,7F));
        products_to_pay.add(new Product("Quarto prodotto",2,8F));
        products_to_pay.add(new Product("Quinto prodotto",1,5.5F));

    }

    @Override
    public void onChange(int i) {
        products_to_pay.remove(i);
        productAdapter = new CheckoutAdapter(products_to_pay,this,this);
        setTotal(products_to_pay);
        recyclerView.setAdapter(productAdapter);

    }
}
