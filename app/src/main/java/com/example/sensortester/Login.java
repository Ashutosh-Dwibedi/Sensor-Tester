package com.example.sensortester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Login extends AppCompatActivity {
    String login_name,login_password,sharedpref_data;
    TextInputEditText login_username,login_user_password;
    AppCompatButton login_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_ok=findViewById(R.id.login_page_ok);
        login_username=findViewById(R.id.login_name_extract_text);
        login_user_password=findViewById(R.id.login_password_extract_text);
        login_ok.setOnClickListener(v -> {
            login_name= Objects.requireNonNull(login_username.getText()).toString();
            login_password=Objects.requireNonNull(login_user_password.getText()).toString();
            if (login_username.getText().toString().isEmpty() || login_user_password.getText().toString().isEmpty()) {
                Toast toast=new Toast(getApplicationContext());
                View view=getLayoutInflater().inflate(R.layout.custom_toast_singnup_layout,findViewById(R.id.custom_toast_layout));
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (!login_username.getText().toString().isEmpty() && !login_user_password.getText().toString().isEmpty()) {
                SharedPreferences preferences_login=getSharedPreferences("userData",MODE_PRIVATE);
                SharedPreferences user_authentication_flag=getSharedPreferences("authentication",MODE_PRIVATE);
                sharedpref_data=preferences_login.getString(login_name,"");
                if (preferences_login.contains(login_name)){
                    if (sharedpref_data.equals(login_password)){
                        SharedPreferences.Editor authentication_editor=user_authentication_flag.edit();
                        authentication_editor.putBoolean("authentication_flag",true);
                        authentication_editor.apply();
                        Intent intent_login=new Intent(getApplicationContext(), HomePage.class);
                        intent_login.putExtra("Username",login_name);
                        startActivity(intent_login);
                        finishAffinity();
                    }
                    else {
                        customDialog();
                    }
                }
                else {
                    customDialog();
                }
            }
        });
    }
    private void customDialog(){
        Dialog login_dialog=new Dialog(Login.this);
        login_dialog.setContentView(R.layout.custom_dialog_layout);
        AppCompatButton dialog_signup_redirect_button=login_dialog.findViewById(R.id.dialog_ok_button_login);
        dialog_signup_redirect_button.setText(R.string.signup);
        TextView dialog_textView=login_dialog.findViewById(R.id.custom_dialog_textView);
        dialog_textView.setText(R.string.no_account_found);
        dialog_signup_redirect_button.setOnClickListener(v1 -> {
            Intent signUp_intent=new Intent(getApplicationContext(), SignUp.class);
            startActivity(signUp_intent);
            login_dialog.dismiss();
        });
        login_dialog.show();
    }
}