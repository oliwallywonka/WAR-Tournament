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
import com.example.wrsw.models.User;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail,edtPassword;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        edtEmail = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        findViewById(R.id.btnLogin).setOnClickListener(this);
    }

    public void loginUser(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

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
        if(password.length()<6){
            edtPassword.setError("La contraseña debe tener minimo 6 caracteres");
            edtPassword.requestFocus();
            return;
        }

        Call<User> call = RetrofitClient
                .getInstance()
                .getApi()
                .loginUser(email,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                    if(response.code()<=299){
                        //String res = response.body().string();
                        user = new User();
                        User user = response.body();
                        Toast.makeText(LoginActivity.this,response.code()+user.token,Toast.LENGTH_LONG).show();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                .putExtra("token",user.token)
                        );
                        finish();
                    }

                    if(response.code()>=300){
                        Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrecta"+response.code(),Toast.LENGTH_LONG).show();
                    }

                    if(response.code()>=500){
                        Toast.makeText(LoginActivity.this,"Error en el servidor",Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.getMessage()+"123",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                loginUser();
                break;
        }
    }

}
