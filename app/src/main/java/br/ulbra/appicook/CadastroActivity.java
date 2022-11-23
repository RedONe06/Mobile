package br.ulbra.appicook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroActivity extends AppCompatActivity{
    private CircleImageView fotoUsuario;
    private Button bt_selecionarFoto, bt_cadastrar;
    private AppCompatButton bt_voltar;
    private EditText txt_nome, txt_Email, txt_senha;
    private TextView txt_mensagemErro;

    private String usuarioID;
    private Uri mSelecionarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        IniciarComponentes();
        txt_nome.addTextChangedListener(cadastroTextWatcher);
        txt_Email.addTextChangedListener(cadastroTextWatcher);
        txt_senha.addTextChangedListener(cadastroTextWatcher);


        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CadastrarUsuario(v);
                Log.d("Btncadastrar","Passou de cadastrar usuário");

            }
        });

        bt_selecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelecionarFotoGaleria();
            }
        });
    }
    public void CadastrarUsuario(View view){

        Log.d("metodo","entrou dentro de cadastrar usuário");
        String email = txt_Email.getText().toString();
        String senha = txt_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    SalvarDadosUsuario();
                    Log.d("metodo","passou do salvar dados do usuario()");
                    Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                    snackbar.show();
                }else {

                    String erro;

                    try {
                        Log.d("firebase","entrou no try");
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Coloque uma senha com no minimo 6 caracteres!";

                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail Invalido";

                    }catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta já foi criada!";

                    }catch (FirebaseNetworkException e){
                        erro = "sem conexão com a Internet!";
                    }catch (Exception e){
                        erro = "Erro ao cadastra o usúario!";
                    }
                    txt_mensagemErro.setText(erro);
                }
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        mSelecionarUri = data.getData();

                        try {
                            fotoUsuario.setImageURI(mSelecionarUri);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void SelecionarFotoGaleria(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }

    public void SalvarDadosUsuario(){

        String nomeArquivo = UUID.randomUUID().toString();

        final StorageReference reference = FirebaseStorage.getInstance().getReference("/imagens/" + nomeArquivo);
        reference.putFile(mSelecionarUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String foto = uri.toString();

                                //Iniciar o banco de dados - Firestore
                                String nome = txt_nome.getText().toString();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                Map<String,Object> usuarios = new HashMap<>();
                                usuarios.put("nome",nome);
                                usuarios.put("foto",foto);

                                usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
                                documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.i("db","Sucesso ao salvar os dados!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Log.i("db_error","Erro ao salvar os dados!" + e.toString());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                            }});
                    }}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                    }});
    }


    public void IniciarComponentes(){
        fotoUsuario = findViewById(R.id.imgFotoPerfil);
        bt_selecionarFoto = findViewById(R.id.btnCadastroEscolherFoto);
        txt_nome = findViewById(R.id.edtCadastroNome);
        txt_Email = findViewById(R.id.edtCadastroEmail);
        txt_senha = findViewById(R.id.edtCadastroSenha);
        txt_mensagemErro = findViewById(R.id.txt_mensagemErro);
        bt_cadastrar = findViewById(R.id.btnCadastroCadastrar);
        bt_voltar = findViewById(R.id.btnCadastroVoltar);
    }

    TextWatcher cadastroTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String nome = txt_nome.getText().toString();
            String email = txt_Email.getText().toString();
            String senha = txt_senha.getText().toString();

            if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()){
                bt_cadastrar.setEnabled(true);
                bt_cadastrar.setBackgroundColor(getResources().getColor(R.color.red_100));
            }else {
                bt_cadastrar.setEnabled(false);
                bt_cadastrar.setBackgroundColor(getResources().getColor(R.color.gray_100));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
