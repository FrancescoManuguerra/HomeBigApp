package com.manuguerra.homebigapp.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.util.Util;
import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.Utilities.UtilSharedPreferences;
import com.manuguerra.homebigapp.datamodels.User;
import com.manuguerra.homebigapp.services.RestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    final static int size_password=6;
    EditText email_register_et;
    EditText password_register_et;
    EditText confirm_password_register_et;
    EditText register_username;
    Button confirm_register_btn;
    private RestController restController;
    private final static String TAG= RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Match xml & java
        email_register_et = findViewById(R.id.email_register_et);
        password_register_et = findViewById(R.id.password_register_et);
        confirm_password_register_et = findViewById(R.id.confirm_password_register_et);
        register_username = findViewById(R.id.register_username);
        confirm_register_btn = findViewById(R.id.confirm_register_btn);
        //Listener
        email_register_et.addTextChangedListener(textWatcher);
        password_register_et.addTextChangedListener(textWatcher);
        confirm_password_register_et.addTextChangedListener(textWatcher);

        //Post request
        restController=new RestController(this);


            confirm_register_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //postRequest
                    Map<String,String>params =new HashMap<>();
                    params.put("username",register_username.getText().toString());
                    params.put("email",email_register_et.getText().toString());
                    params.put("password",password_register_et.getText().toString());

                    restController.postRequest("auth/local/register", RegisterActivity.this,RegisterActivity.this,params );

                }
            });
    }
    //for watching changes
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = email_register_et.getText().toString().trim();
            String password = password_register_et.getText().toString().trim();
            String confirm_password = confirm_password_register_et.getText().toString().trim();
            confirm_register_btn.setEnabled(emailControll(email) && passwordControll(password,confirm_password));

        }
        @Override
        public void afterTextChanged(Editable s) {
            Log.i("RegisterAfter",s.toString());
        }
        };

    //email syntax controll
    private boolean emailControll(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //password syntax controll
    private boolean passwordControll(String password,String confirm_password){
        //6 CARATTERI
        if(password.length()>=size_password){
            if(confirm_password.equals(password))return true;
            else return false;
        }
        else  return false;
    }




    //Post error Response
    @Override
    public void onErrorResponse(VolleyError error) {

        Log.i("RegisterActivity", error.getMessage());
    }

    //Post goog Response
    @Override
    public void onResponse(String response)  {
        try {
            Log.i("RegisterActivity", "CIAOOO"+response.toString());
            JSONObject responseJson= new JSONObject(response);
            String accessToken=responseJson.getString("jwt");

            User user=new User(responseJson.getJSONObject("user"),accessToken);

            //sharedPreferences
            UtilSharedPreferences.editor=getSharedPreferences("saves",MODE_PRIVATE).edit();
            UtilSharedPreferences.editor.putString("token",accessToken);
            UtilSharedPreferences.editor.putString("email",user.getEmail());
            User.accessToken=accessToken;
            User.email=user.getEmail();
            User.username=user.getEmail();
            UtilSharedPreferences.editor.apply();

            setResult(Activity.RESULT_OK);
            finish();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
