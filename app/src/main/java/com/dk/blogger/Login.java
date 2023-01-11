package com.dk.blogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.dk.blogger.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        //boolean newuser=getIntent().getExtras().getBoolean(Signup.NEWUSER);
        boolean newuser=getIntent().getBooleanExtra(Signup.NEWUSER,false);
        if(newuser){
            Toast.makeText(this, "Registration successful...", Toast.LENGTH_SHORT).show();
        }
        else{
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loginBinding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBinding.progressBar.setVisibility(View.VISIBLE);
                String email=loginBinding.emailtxt.getText().toString();
                String pass=loginBinding.passtxt.getText().toString();
                authenticateUser(email,pass);
            }
        });
        loginBinding.sigupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });

        loginBinding.loginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,PhoneLogin.class));
                finish();
            }
        });
    }

    private void authenticateUser(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loginBinding.progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            updateUI(currentUser);
                        }else{
                            loginBinding.progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Authentication failed."+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            Intent intent=new Intent(Login.this,MainActivity.class);
            intent.putExtra("email",currentUser.getEmail());
            intent.putExtra("uid",currentUser.getUid());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }else{
            Toast.makeText(this, "NO USERS...", Toast.LENGTH_SHORT).show();
        }
    }
}