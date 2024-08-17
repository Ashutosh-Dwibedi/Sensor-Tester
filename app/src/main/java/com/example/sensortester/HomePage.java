package com.example.sensortester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    MaterialButton logout_button,delete_account_button;
    MaterialTextView home_username;
    String retrieved_username;
    String final_username;
    AppCompatImageButton light_sensor_button,proximity_sensor_button,fingerprint_sensor_button,gyro_sensor_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent get_intent=getIntent();
        retrieved_username=get_intent.getStringExtra("Username");
        if (!Objects.equals(retrieved_username, "from_sensor_work")&&!Objects.equals(retrieved_username,"from_initial_activity")){
            SharedPreferences temp_pref=getSharedPreferences("loggedUser",MODE_PRIVATE);
            SharedPreferences.Editor key_editor=temp_pref.edit();
            key_editor.putString("user",retrieved_username);
            key_editor.apply();
        }
        SharedPreferences user_authentication_flag=getSharedPreferences("authentication",MODE_PRIVATE);
        boolean flag_value_home=user_authentication_flag.getBoolean("authentication_flag",false);
        if (flag_value_home){
            SharedPreferences verification_pref=getSharedPreferences("loggedUser",MODE_PRIVATE);
            final_username=verification_pref.getString("user","");
        }
        logout_button=findViewById(R.id.logout_button);
        delete_account_button=findViewById(R.id.delete_button);
        home_username=findViewById(R.id.username_home_page);
        if (!Objects.equals(final_username, ""))
            home_username.setText(final_username);
        light_sensor_button=findViewById(R.id.light_sensor);
        proximity_sensor_button=findViewById(R.id.proximity_sensor);
        fingerprint_sensor_button=findViewById(R.id.fingerprint_sensor);
        gyro_sensor_button=findViewById(R.id.gyro_sensor);
        logout_button.setOnClickListener(this);
        delete_account_button.setOnClickListener(this);
        light_sensor_button.setOnClickListener(this);
        proximity_sensor_button.setOnClickListener(this);
        fingerprint_sensor_button.setOnClickListener(this);
        gyro_sensor_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences pref=getSharedPreferences("userData",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        Intent intent=new Intent(getApplicationContext(), SensorWork.class);
        if (v.getId()==light_sensor_button.getId()){
            intent.putExtra("Sensor_name","Light");
            startActivity(intent);
        } else if (v.getId()==proximity_sensor_button.getId()) {
            intent.putExtra("Sensor_name","Proximity");
            startActivity(intent);
        } else if (v.getId()==fingerprint_sensor_button.getId()) {
            intent.putExtra("Sensor_name","Fingerprint");
            startActivity(intent);
        } else if (v.getId()==gyro_sensor_button.getId()) {
            intent.putExtra("Sensor_name","Gyro");
            startActivity(intent);
        } else if (v.getId()==logout_button.getId()) {
            SharedPreferences user_authentication_flag=getSharedPreferences("authentication",MODE_PRIVATE);
            SharedPreferences.Editor authentication_editor=user_authentication_flag.edit();
            authentication_editor.putBoolean("authentication_flag",false);
            authentication_editor.apply();
            SharedPreferences temp_pref_logout=getSharedPreferences("loggedUser",MODE_PRIVATE);
            SharedPreferences.Editor clear_editor_logout=temp_pref_logout.edit();
            clear_editor_logout.clear();
            clear_editor_logout.apply();
            Intent logout_intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(logout_intent);
            finishAffinity();
        } else if (v.getId()==delete_account_button.getId()) {
            SharedPreferences user_authentication_flag=getSharedPreferences("authentication",MODE_PRIVATE);
            SharedPreferences.Editor authentication_editor=user_authentication_flag.edit();
            authentication_editor.putBoolean("authentication_flag",false);
            authentication_editor.apply();
            Intent delete_intent=new Intent(getApplicationContext(),MainActivity.class);
            editor.remove(retrieved_username);
            editor.apply();
            SharedPreferences temp_pref_delete=getSharedPreferences("loggedUser",MODE_PRIVATE);
            SharedPreferences.Editor clear_editor_delete=temp_pref_delete.edit();
            clear_editor_delete.clear();
            clear_editor_delete.apply();
            startActivity(delete_intent);
            finishAffinity();
        }
    }
}