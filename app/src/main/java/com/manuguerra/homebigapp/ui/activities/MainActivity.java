package com.manuguerra.homebigapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.Utilities.UtilSharedPreferences;
import com.manuguerra.homebigapp.datamodels.Restaurant;
import com.manuguerra.homebigapp.interfaces.StartActivityInterface;
import com.manuguerra.homebigapp.services.RestController;
import com.manuguerra.homebigapp.ui.adapter.ModelAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StartActivityInterface, Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean gridOn = false;
    private RecyclerView mRecyclerView;
    private ModelAdapter modelAdapter;
    private RestController restController;
    private boolean isLog;
    private String accessToken;
    private String email;
    private Menu menu;
    private static final int LOGIN_REQUEST_CODE = 2001;

    private int numberColumn = 2;
    public ArrayList<Restaurant> dati = new ArrayList<Restaurant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restController = new RestController(this);
        restController.getRequest(Restaurant.ENDPOINT, this, this);

        //Set layout
        UtilSharedPreferences.sharedPreferences = getSharedPreferences("saves", MODE_PRIVATE);
        UtilSharedPreferences.sharedPreferences.getString("email", email);
        if (UtilSharedPreferences.sharedPreferences.getString("token", accessToken) != null) {
            isLog = true;
            accessToken = UtilSharedPreferences.sharedPreferences.getString("token", accessToken);

            Toast.makeText(MainActivity.this, UtilSharedPreferences.sharedPreferences.getString("email", email), Toast.LENGTH_LONG).show();

        } else Toast.makeText(MainActivity.this, "LOG IN!", Toast.LENGTH_LONG).show();

        gridOn = UtilSharedPreferences.sharedPreferences.getBoolean("visualization", gridOn);
        //Bind recyclerView
        mRecyclerView = findViewById(R.id.recycler_view);

        //set LayoutManager
        if (gridOn) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberColumn));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        modelAdapter = new ModelAdapter(dati, this, gridOn, this);
        mRecyclerView.setAdapter(modelAdapter);
        modelAdapter.notifyDataSetChanged();


    }

    @Override
    public void startShoptActivity(Restaurant data) {
        Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("id_restaurant", data.getId());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //TODO login is successfull manage result

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        if (UtilSharedPreferences.sharedPreferences.getString("token", accessToken) != null) {
            menu.findItem(R.id.login_menu).setVisible(false);
            menu.findItem(R.id.register_menu).setVisible(false);
        } else {
            menu.findItem(R.id.user_menu).setVisible(false);
            menu.findItem(R.id.logout_menu).setVisible(false);
        }
        menu.findItem(R.id.change_menu).setIcon(gridOn ? R.drawable.ic_view_comfy_black_24dp : R.drawable.ic_view_compact_black_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout_menu) {
            UtilSharedPreferences.sharedPreferences = getSharedPreferences("saves", MODE_PRIVATE);
            UtilSharedPreferences.sharedPreferences.edit().remove("token").commit();
            UtilSharedPreferences.sharedPreferences.edit().remove("email").commit();

        }
        if (item.getItemId() == R.id.user_menu) {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.register_menu) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.login_menu) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
        if (item.getItemId() == R.id.change_menu) {

            if (gridOn) {
                gridOn = false;
                //Set share preferences
                UtilSharedPreferences.editor = getSharedPreferences("saves", MODE_PRIVATE).edit();
                UtilSharedPreferences.editor.putBoolean("visualization", gridOn);
                UtilSharedPreferences.editor.apply();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                modelAdapter = new ModelAdapter(dati, this, gridOn, this);
                mRecyclerView.setAdapter(modelAdapter);
                item.setIcon(R.drawable.ic_view_comfy_black_24dp);
            } else {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    numberColumn = 4;
                } else {
                    numberColumn = 2;
                }
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberColumn, GridLayoutManager.VERTICAL, false));
                gridOn = true;
                //set share preferences
                UtilSharedPreferences.editor = getSharedPreferences("saves", MODE_PRIVATE).edit();
                UtilSharedPreferences.editor.putBoolean("visualization", gridOn);
                UtilSharedPreferences.editor.apply();

                modelAdapter = new ModelAdapter(dati, this, gridOn, this);
                mRecyclerView.setAdapter(modelAdapter);
                item.setIcon(R.drawable.ic_view_compact_black_24dp);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("TAG", error.getMessage());
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONArray restaurantjsonArray = new JSONArray(response);
            for (int i = 0; i < restaurantjsonArray.length(); i++) {
                Restaurant restaurant = new Restaurant(restaurantjsonArray.getJSONObject(i));
                dati.add(restaurant);
            }
            modelAdapter.setData(dati);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
