package com.example.sensortester;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    String name, password, confirm_password;
    AppCompatButton signUp_ok;
    TextInputEditText signup_username, signup_password, signup_confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUp_ok=findViewById(R.id.signup_page_ok);
        signup_username=findViewById(R.id.signup_name_extract_text);
        signup_password=findViewById(R.id.signup_password_extract_text);
        signup_confirm_password=findViewById(R.id.signup_password_confirm_extract_text);
        signUp_ok.setOnClickListener(v -> {
            name= Objects.requireNonNull(signup_username.getText()).toString();
            password= Objects.requireNonNull(signup_password.getText()).toString();
            confirm_password= Objects.requireNonNull(signup_confirm_password.getText()).toString();
            if (signup_username.getText().toString().isEmpty() || signup_password.getText().toString().isEmpty()){
                Toast toast=new Toast(getApplicationContext());
                View view=getLayoutInflater().inflate(R.layout.custom_toast_singnup_layout,findViewById(R.id.custom_toast_layout));
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (!signup_username.getText().toString().isEmpty() && !signup_password.getText().toString().isEmpty()){
                if (Objects.equals(password, confirm_password)){
                    SharedPreferences preferences=getSharedPreferences("userData",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    if (!preferences.contains(name)){
                        editor.putString(name,password);
                        editor.apply();
                        SharedPreferences user_authentication_flag=getSharedPreferences("authentication",MODE_PRIVATE);
                        SharedPreferences.Editor authentication_editor=user_authentication_flag.edit();
                        authentication_editor.putBoolean("authentication_flag",true);
                        authentication_editor.apply();
                        Intent intent=new Intent(getApplicationContext(), HomePage.class);
                        intent.putExtra("Username",name);
                        startActivity(intent);
                        finishAffinity();
                    }
                    else {
                        signUpDialog();
                    }
                }
                else {
                    signup_confirm_password.setError("Password doesn't match!");
                    new Handler().postDelayed(() -> signup_confirm_password.setError(null),2000);
                }
            }
        });
    }
    private void signUpDialog(){
        Dialog signup_dialog=new Dialog(SignUp.this);
        signup_dialog.setContentView(R.layout.custom_dialog_layout);
        AppCompatButton dialog_login_redirect_button=signup_dialog.findViewById(R.id.dialog_ok_button_login);
        dialog_login_redirect_button.setOnClickListener(v1 -> {
            Intent login_intent=new Intent(getApplicationContext(), Login.class);
            startActivity(login_intent);
            signup_dialog.dismiss();
        });
        signup_dialog.show();
    }
}