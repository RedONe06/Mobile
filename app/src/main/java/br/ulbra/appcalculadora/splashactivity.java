package br.ulbra.appcalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class splashactivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);
        webView = findViewById(R.id.imgGif);

        Handler handler = new Handler();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://cdn.dribbble.com/users/218217/screenshots/3610718/first_scene_blue_800x600_2_dribbble.gif");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarMainActivity();
            }
        }, 5000);

    }

    private void mostrarMainActivity() {
        Intent intent = new Intent(
                splashactivity.this, MainActivity.class
        );
        startActivity(intent);
        finish();
    }
}