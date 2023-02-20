package com.example.ychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ychat.Models.Users;
import com.example.ychat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        progressDialog= new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're Creatin Your Account");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if (!binding.txtUserName.getText().toString().isEmpty()&&
                        !binding.txtEmail.getText().toString().isEmpty() &&!binding.txtPassword.getText().toString().isEmpty()){

                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),
                            binding.txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Users user = new Users(binding.txtUserName.getText().toString(),binding.txtPassword.getText().toString(),
                                        binding.txtEmail.getText().toString());
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(user);

                                Toast.makeText(SignUpActivity.this, "Sign Up Succesful", Toast.LENGTH_SHORT).show( );
                            }
                            else{
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show( );
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(SignUpActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show( );

                }
            }
        });

        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( SignUpActivity.this,SignInActivity.class );
                startActivity(intent);
            }
        });
    }
}