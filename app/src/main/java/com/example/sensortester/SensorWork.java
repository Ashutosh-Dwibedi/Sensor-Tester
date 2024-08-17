package com.example.sensortester;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;
import java.util.concurrent.Executor;

public class SensorWork extends AppCompatActivity implements SensorEventListener {
    ConstraintLayout child_layout;
    String sensor_clicked;
    SensorManager sensorManager;
    AppCompatImageView central_image;
    Animation blink_animation;
    Sensor  light_sensor,proximity_sensor,gyro_sensor;
    boolean flag=false;
    TextView common_data_view;
    MediaPlayer proximity_close,too_much_light,pumpkin_bat_sound;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    BiometricManager biometricManager;
    LottieAnimationView gyro_flying_pumpkin_bat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_work);
        Intent retrieved_intent=getIntent();
        sensor_clicked=retrieved_intent.getStringExtra("Sensor_name");
        child_layout=findViewById(R.id.sensor_work_child_layout_1);
        central_image=findViewById(R.id.central_image);
        common_data_view=findViewById(R.id.common_textView);
        gyro_flying_pumpkin_bat=findViewById(R.id.gyro_pumpkin_bat);
        blink_animation= AnimationUtils.loadAnimation(this,R.anim.blink_animation);
        proximity_close=MediaPlayer.create(this,R.raw.proximity_close_sound);
        too_much_light=MediaPlayer.create(this,R.raw.light_too_much_sound);
        pumpkin_bat_sound=MediaPlayer.create(this,R.raw.pumpkin_bats_sound);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager!=null){
            light_sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            proximity_sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            gyro_sensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            switch(Objects.requireNonNull(sensor_clicked)){
                case "Light":
                    if (light_sensor!=null){
                        sensorManager.registerListener(this,light_sensor,SensorManager.SENSOR_DELAY_UI);
                    }
                    else{
                        Toast.makeText(this, "No sensor detected!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Proximity":
                    if (proximity_sensor!=null) {
                        sensorManager.registerListener(this,proximity_sensor,SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    else{
                        Toast.makeText(this, "No sensor detected!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Fingerprint":
                    biometricManager=BiometricManager.from(getApplicationContext());
                    switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                            customToastMethod("Device doesn't have fingerprint sensor!");
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                            customToastMethod("Device fingerprint sensor is not working!");
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                            customToastMethod("No fingerprint assigned!");
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                            customToastMethod("Security update required!");
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                            customToastMethod("Fingerprint is unsupported!");
                            break;
                        case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                            customToastMethod("Fingerprint status unknown!");
                            break;
                        case BiometricManager.BIOMETRIC_SUCCESS:
                            Executor executor= ContextCompat.getMainExecutor(this);
                            biometricPrompt=new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                                @Override
                                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                    super.onAuthenticationError(errorCode, errString);
                                }

                                @Override
                                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                    super.onAuthenticationSucceeded(result);
                                    customDialog();
                                }

                                @Override
                                public void onAuthenticationFailed() {
                                    super.onAuthenticationFailed();
                                }
                            });
                            promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication")
                                    .setSubtitle("Use your fingerprint.").setNegativeButtonText("Cancel")
                                    //.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG).build();
                            biometricPrompt.authenticate(promptInfo);
                            break;
                    }
                    break;
                case "Gyro":
                    if (gyro_sensor!=null) {
                        sensorManager.registerListener(this,gyro_sensor,SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    else{
                        Toast.makeText(this, "Sensor is not working!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (too_much_light.isPlaying())
                    too_much_light.release();
                else if (proximity_close.isPlaying())
                    proximity_close.release();
                else if (pumpkin_bat_sound.isPlaying())
                    pumpkin_bat_sound.release();
                sensorManager.unregisterListener(SensorWork.this);
                finish();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if (event.values[0]>0){
                defaultLayoutValue("Safe Distance!");
                if (proximity_close.isPlaying())
                    proximity_close.pause();
            }
            else{
                central_image.setVisibility(View.VISIBLE);
                gyro_flying_pumpkin_bat.setVisibility(View.GONE);
                child_layout.setBackgroundColor(Color.parseColor("#ff2700"));
                central_image.setImageResource(R.drawable.baseline_do_not_touch_24);
                child_layout.startAnimation(blink_animation);
                common_data_view.setText(R.string.too_close);
                common_data_view.setBackgroundColor(Color.parseColor("#cb0000"));
                proximity_close.start();
                proximity_close.setLooping(true);
            }

        }
        else if (event.sensor.getType()==Sensor.TYPE_LIGHT){
            int light_sensor_data=(int)event.values[0];
            if (light_sensor_data>=0 && light_sensor_data<100){
                defaultLayoutValue("Dark");
                if (too_much_light.isPlaying())
                    too_much_light.pause();
                flag=false;
            } else if (light_sensor_data>=100 && light_sensor_data<500) {
                defaultLayoutValue("Light Dark");
                if (too_much_light.isPlaying())
                    too_much_light.pause();
                flag=false;
            } else if (light_sensor_data>=500 && light_sensor_data<1000) {
                defaultLayoutValue("Normal");
                if (too_much_light.isPlaying())
                    too_much_light.pause();
                flag=false;
            } else if (light_sensor_data>=1000 && light_sensor_data<1800) {
                defaultLayoutValue("Incredibly Bright");
                if (too_much_light.isPlaying())
                    too_much_light.pause();
                flag=false;
            } else if (light_sensor_data>=1800) {
                if (!flag){
                    child_layout.setVisibility(View.VISIBLE);
                    central_image.setVisibility(View.VISIBLE);
                    gyro_flying_pumpkin_bat.setVisibility(View.GONE);
                    common_data_view.setBackgroundColor(Color.parseColor("#cb0000"));
                    child_layout.setBackgroundColor(Color.parseColor("#ff2700"));
                    child_layout.startAnimation(blink_animation);
                    too_much_light.start();
                    too_much_light.setLooping(true);
                    flag=true;
                }
                common_data_view.setText(R.string.dangerously_bright_light);
            }
        } else if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
            child_layout.setVisibility(View.VISIBLE);
            gyro_flying_pumpkin_bat.setVisibility(View.VISIBLE);
            gyro_flying_pumpkin_bat.setRotation(-event.values[2]*10f);
            gyro_flying_pumpkin_bat.setRotationX(event.values[0]*10f);
            gyro_flying_pumpkin_bat.setRotationY(event.values[1]*10f);
            gyro_flying_pumpkin_bat.setTranslationX(event.values[0]);
            gyro_flying_pumpkin_bat.setTranslationY(event.values[1]);
            pumpkin_bat_sound.start();
            pumpkin_bat_sound.setLooping(true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void customToastMethod(String text){
        Toast toast=new Toast(this);
        View view=getLayoutInflater().inflate(R.layout.custom_toast_singnup_layout,findViewById(R.id.custom_toast_layout));
        toast.setView(view);
        TextView toast_textView=view.findViewById(R.id.toast_textview);
        toast_textView.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
    private void customDialog(){
        Dialog fingerprint_dialog=new Dialog(this);
        fingerprint_dialog.setContentView(R.layout.custom_dialog_layout);
        AppCompatButton go_back_button=fingerprint_dialog.findViewById(R.id.dialog_ok_button_login);
        go_back_button.setText(R.string.go_back);
        TextView dialog_textView=fingerprint_dialog.findViewById(R.id.custom_dialog_textView);
        dialog_textView.setText(R.string.fingerprint_is_verified);
        go_back_button.setOnClickListener(v1 -> {
            Intent homePage_intent=new Intent(getApplicationContext(), HomePage.class);
            homePage_intent.putExtra("Username","from_sensor_work");
            startActivity(homePage_intent);
            fingerprint_dialog.dismiss();
            finishAffinity();
        });
        fingerprint_dialog.setCancelable(false);
        fingerprint_dialog.show();
    }
    private  void defaultLayoutValue(String set_text){
        central_image.setVisibility(View.GONE);
        gyro_flying_pumpkin_bat.setVisibility(View.GONE);
        child_layout.setBackground(null);
        child_layout.setAnimation(null);
        common_data_view.setBackgroundResource(R.drawable.gradient_background);
        common_data_view.setText(set_text);
    }
}