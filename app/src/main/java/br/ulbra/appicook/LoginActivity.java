package br.ulbra.appicook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class LoginActivity extends AppCompatActivity {

    ImageView imageView;
    AppCompatButton btnCadastrar, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.logo_login);
        btnCadastrar = findViewById(R.id.btnLoginCadastrar);

        // Adding the gif here using glide library
        Glide.with(this).load(R.drawable.logo).into(imageView);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        LoginActivity.this, CadastroActivity.class
                );
                startActivity(intent);
            }
        });
    }


}