package com.example.wrsw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wrsw.R;
import com.example.wrsw.api.RetrofitClient;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEmail,edtPassword,edtName;
    private Button btnCreate,btnLog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);

        edtEmail = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.edtName);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnCreate).setOnClickListener(this);
    }

    public void userSignUp(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String name = edtName.getText().toString().trim();


        if(email.isEmpty()){
            edtEmail.setError("El Email es requerido");
            edtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Ingrese un email Valido");
            edtEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            edtPassword.setError("El Password es requerido");
            edtPassword.requestFocus();
            return;
        }
        if(name.isEmpty()){
            edtName.setError("El Nombre es requerido");
            edtName.requestFocus();
            return;
        }
        if(password.length()<6){
            edtPassword.setError("La contraseña debe tener minimo 6 caracteres");
            edtPassword.requestFocus();
            return;
        }

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(email,password,name);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.code()<205){
                        String res = response.body().string();
                        Toast.makeText(CreateUserActivity.this,res,Toast.LENGTH_LONG).show();
                    }

                    if(response.code()>399 || response.code()<500){
                        Toast.makeText(CreateUserActivity.this,"El usuario ya existe o peticion negada",Toast.LENGTH_LONG).show();
                    }

                    if(response.code()>499){
                        Toast.makeText(CreateUserActivity.this,"Error en el servidor",Toast.LENGTH_LONG).show();
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateUserActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                userSignUp();
                break;
            case R.id.btnLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

}
