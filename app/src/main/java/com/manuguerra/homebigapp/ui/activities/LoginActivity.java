package com.manuguerra.homebigapp.ui.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.Utilities.UtilSharedPreferences;
import com.manuguerra.homebigapp.datamodels.Restaurant;
import com.manuguerra.homebigapp.datamodels.User;
import com.manuguerra.homebigapp.services.RestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>,Response.ErrorListener {

    public final static String EMAIL_KEY = "email";
    public final static String PASSWORD_KEY = "password";
    final static int size_password = 6;
    private RestController restController;
    private TextView login;
    private EditText email_et;
    private EditText password_et;
    private Button login_btn;
    private Button register_btn;
    private Switch login_switch;
    private LinearLayout activity_main;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //attach to this Activity the activity_layout.xml file
        setContentView(R.layout.activity_login);

        //dichiarazione componenti xml
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        login_btn = findViewById(R.id.login_btn);
        register_btn = findViewById(R.id.register_btn);
        login = findViewById(R.id.login);
        activity_main = findViewById(R.id.activity_main_layout);

        //settaggio listener
       login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
            }
        });

    }


    private void doLogin() {

        String email = email_et.getText().toString();
        String password = password_et.getText().toString();


        if (emailControll(email) && passwordControll(password)) {

            Map<String,String> params= new HashMap<>();
            params.put("identifier",email);
            params.put("password",password);
            restController=new RestController(this);
            restController.postRequest("auth/local", this,this, params);

        } else {
            Toast.makeText(this, getString(R.string.error_email), Toast.LENGTH_LONG).show();
        }
    }

    private boolean emailControll(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean passwordControll(String password) {
        //6 CARATTERI
        if (password.length() <= size_password) return false;
        else return true;
    }

        @Override
        public void onErrorResponse(VolleyError error) {

            Log.e("TAG", error.getMessage());
            Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResponse(String response) {
        //TO DO
            try {
                Log.d("rispostaJSON", response.toString());
                JSONObject jsonObject=new JSONObject(response);
                String accessToken=jsonObject.getString("jwt");
                JSONObject jsonUser= jsonObject.getJSONObject("user");
                if(jsonUser.getString("confirmed").equals("true")){

                    UtilSharedPreferences.editor=getSharedPreferences("saves",MODE_PRIVATE).edit();
                    UtilSharedPreferences.editor.putString("token",accessToken);
                    UtilSharedPreferences.editor.putString("email",jsonUser.getString("email"));
                    User.accessToken=accessToken;
                    User.email=jsonUser.getString("email");
                    User.username=jsonUser.getString("username");

                    UtilSharedPreferences.editor.apply();
                    setResult(Activity.RESULT_OK);
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }