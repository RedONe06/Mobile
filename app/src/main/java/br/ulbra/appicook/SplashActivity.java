package br.ulbra.appicook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();

        imageView = findViewById(R.id.imageview);

        // Adding the gif here using glide library
        Glide.with(this).load(R.drawable.splash).into(imageView);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarMainActivity();
            }
        }, 5000);
    }

    private void mostrarMainActivity() {
        Intent intent = new Intent(
                SplashActivity.this, LoginActivity.class
        );
        startActivity(intent);
        finish();
    }
}