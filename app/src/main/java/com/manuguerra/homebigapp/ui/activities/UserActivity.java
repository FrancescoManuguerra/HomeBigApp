package com.manuguerra.homebigapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.Utilities.UtilSharedPreferences;
import com.manuguerra.homebigapp.datamodels.User;

public class UserActivity extends AppCompatActivity {

    private TextView email_user_tv;
    private TextView token_user;
    private String email;
    private String accessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        email_user_tv=findViewById(R.id.email_user_tv);
        token_user=findViewById(R.id.token_user);

        UtilSharedPreferences.sharedPreferences=getSharedPreferences("saves",MODE_PRIVATE);

        email_user_tv.setText(UtilSharedPreferences.sharedPreferences.getString("email",email));
        token_user.setText(UtilSharedPreferences.sharedPreferences.getString("token",accessToken));


    }
}
