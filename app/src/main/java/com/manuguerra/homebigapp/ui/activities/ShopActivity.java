package com.manuguerra.homebigapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.manuguerra.homebigapp.Utilities.UtilSharedPreferences;
import com.manuguerra.homebigapp.datamodels.Product;
import com.manuguerra.homebigapp.datamodels.Restaurant;
import com.manuguerra.homebigapp.datamodels.User;
import com.manuguerra.homebigapp.services.RestController;
import com.manuguerra.homebigapp.ui.adapter.ProductAdapter;
import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.interfaces.onQuantityChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements onQuantityChangedListener, Response.Listener<String>,Response.ErrorListener {

    private TextView ordine_minimo_tv;
    private TextView name_restaurant_checkout_tv;
    private TextView total_shop_tv;
    private Button checkout_btn;
    private String idRestaurant;
    private RecyclerView recyclerView;
    private ImageView img_restaurant_shop_iv;
    public  ArrayList<Product> products=new ArrayList<>();
    private ProductAdapter productAdapter=new ProductAdapter(products, this);
    private static final int LOGIN_REQUEST_CODE = 2001;
    private static final int REGISTE_REQUEST_CODE = 2002;


    private float minimoOrdine;
    private float total;
    private RestController restController;
    private ProgressBar progressBar;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_relative);

        //INTENT
        Intent intent = getIntent();
        idRestaurant=intent.getStringExtra("id_restaurant");
        Log.e("error","after intent ");
        //Set component
        img_restaurant_shop_iv=findViewById(R.id.img_restaurant_shop_iv);
        checkout_btn = findViewById(R.id.checkout_btn);
        total_shop_tv = findViewById(R.id.total_shop_tv);
        ordine_minimo_tv = findViewById(R.id.ordine_minimo_tv);
        name_restaurant_checkout_tv = findViewById(R.id.name_restaurant_checkout_tv);
        progressBar = findViewById(R.id.progress_shop_pb);
        recyclerView = findViewById(R.id.recycler_view_shop);


        //Rest query
        restController=new RestController(this);
        restController.getRequest(Restaurant.ENDPOINT.concat(idRestaurant),this,this);


        //LAYOUT
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //error here
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnQuantityChangedListener(this);
        productAdapter.notifyDataSetChanged();
        total_shop_tv.addTextChangedListener(textWatcher);
        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UtilSharedPreferences.sharedPreferences=getSharedPreferences("saves",MODE_PRIVATE);
                if(UtilSharedPreferences.sharedPreferences.getString("token",User.accessToken)!=null){
                    Intent intent=new Intent(ShopActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                }else{
                    startActivityForResult(new Intent(ShopActivity.this,LoginActivity.class),LOGIN_REQUEST_CODE);
                }

            }
        });


    }

        //Text watcher for update progress bar
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkout_btn.setEnabled(total >= minimoOrdine);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //TODO login is successfull manage result

            menu.findItem(R.id.login_menu).setTitle(R.string.profile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(ShopActivity.this,UserActivity.class));
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        this.menu=menu;
        if(UtilSharedPreferences.sharedPreferences.getString("token", User.accessToken)!=null){
            menu.findItem(R.id.login_menu).setVisible(false);
            menu.findItem(R.id.register_menu).setVisible(false);
        }else{
            menu.findItem(R.id.user_menu).setVisible(false);
            menu.findItem(R.id.logout_menu).setVisible(false);
        }
        menu.findItem(R.id.change_menu).setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.login_menu){
            startActivityForResult(new Intent(ShopActivity.this,LoginActivity.class),LOGIN_REQUEST_CODE);
        }
        if(item.getItemId()==R.id.register_menu){
            startActivityForResult(new Intent(ShopActivity.this,RegisterActivity.class),REGISTE_REQUEST_CODE);
        }
        if (item.getItemId()==R.id.logout_menu){
            UtilSharedPreferences.sharedPreferences=getSharedPreferences("saves",MODE_PRIVATE);
            UtilSharedPreferences.sharedPreferences.edit().remove("token").commit();
            UtilSharedPreferences.sharedPreferences.edit().remove("email").commit();
        }
        if(item.getItemId()==R.id.user_menu){
         startActivity(new Intent(ShopActivity.this,UserActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTotal(float item){
        total+=item;
        total_shop_tv.setText(String.valueOf(total));

    }
    private void updateProgress(int progress){
        progressBar.setProgress(progress);
    }

    @Override
    public void onChange(float price) {
        updateTotal(price);
        updateProgress((int)total*100);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("error", "ON ERROR RESPONSE");
    }

    @Override
    public void onResponse(String response) {

        try {
            JSONObject jsonObject=new JSONObject(response);
            Restaurant restaurant=new Restaurant(jsonObject);
            Log.i("ShopActivity",String.valueOf(jsonObject.getDouble("min_order")));
            JSONArray productjsonArray= jsonObject.getJSONArray("products");
            minimoOrdine=(float)(jsonObject.getDouble("min_order"));
            ordine_minimo_tv.setText(ordine_minimo_tv.getText()+" "+String.valueOf(minimoOrdine));
            progressBar.setMax((int) jsonObject.getDouble("min_order") * 100);
            for(int i=0; i<productjsonArray.length();i++){
                Product product=new Product(productjsonArray.getJSONObject(i));
                products.add(product);
                recyclerView.setAdapter(productAdapter);
            }
            productAdapter.setDataProduct(products);
            productAdapter.setDataRestaurant(restaurant);
            Log.i("IMG","before glide");
            RequestManager requestManager = Glide.with(ShopActivity.this);
            Log.i("IMG", "after glide");
            RequestBuilder requestBuilder = requestManager.load(jsonObject.getString("image_url"));
            Log.i("IMG","after load");
            requestBuilder.into(img_restaurant_shop_iv);
            Log.i("IMG","after Into");

        } catch (JSONException e) {
            Log.e("ShopActivity",e.getMessage());
        }
    }
}



