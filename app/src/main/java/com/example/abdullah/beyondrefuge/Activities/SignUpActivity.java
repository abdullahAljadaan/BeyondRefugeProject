package com.example.abdullah.beyondrefuge.Activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.abdullah.beyondrefuge.R;
import com.example.abdullah.beyondrefuge.Modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    EditText etEmailSignUp;

    EditText etPasswordSignUp;

    EditText etPassordConformation;
    Button logInPagePag;
    Button signUp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDataBase;
    private DatabaseReference databaserefernce;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        etEmailSignUp = (EditText) findViewById(R.id.et_signup_email);
        etPasswordSignUp = (EditText)findViewById(R.id.et_signup_password);
        etPassordConformation = (EditText)findViewById(R.id.et_login_confirm_password);

        checkingInternetConnection();

        mAuth = FirebaseAuth.getInstance();
        mDataBase= FirebaseDatabase.getInstance();
        databaserefernce= mDataBase.getReference();

        logInPagePag = (Button)findViewById(R.id.btn_log_in_page);
        logInPagePag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoginPag = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intentLoginPag);
                finish();
            }
        });

        signUp = (Button) findViewById(R.id.btn_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatAccount(v);
            }


        });
    }


    public void creatAccount(View view ){
        String emailSignUp = etEmailSignUp.getText().toString();
        String passwordSgnUp = etPasswordSignUp.getText().toString();
        String passwordConfirmatin= etPassordConformation.getText().toString();
        if (emailSignUp.isEmpty()){
            etEmailSignUp.setError("Email is requaired!");
            etEmailSignUp.requestFocus();
        return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailSignUp).matches()){
            etEmailSignUp.setError("Please enter a valid email !");
            etEmailSignUp.requestFocus();
            return;
        }
        if (passwordSgnUp.isEmpty()){
            etPasswordSignUp.setError("Password is requaired !");
            etPasswordSignUp.requestFocus();
            return;
        }
        if(passwordSgnUp.length() < 6){
            etPasswordSignUp.setError("Please choose a password with 6 charechters at least!");
            etPasswordSignUp.requestFocus();
            return;
        }
        if(! passwordConfirmatin.equals(passwordSgnUp)){
            etPassordConformation.setError("Please make sure of your password !");
            etPassordConformation.requestFocus();
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(emailSignUp, passwordSgnUp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String emailSignUp = String.valueOf(etEmailSignUp.getText());
                    if (task.isSuccessful()) {
                        writeNewUser(null, emailSignUp, false);
                        Intent intentRef = new Intent(SignUpActivity.this, PreferncesActivity.class);
                        startActivity(intentRef);
                        finish();
                    } else if (task.getException() instanceof FirebaseAuthException) {
                        Toast.makeText(getApplicationContext(), "You are alraedy registerd !", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private void writeNewUser(String name , String email , boolean isTagCompleted) {
        String userId = String.valueOf(new Date().getTime());
        User user = new User(userId , name , email, isTagCompleted);
      databaserefernce.child("users").child(userId).setValue(user);


    }

    private void checkingInternetConnection() {
        if(isOnline()){

        }
        else{
            new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle(R.string.alert_connection_1)
                    .setMessage(R.string.alert_connection_2)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                        }
                    }).show();

        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nIfo = cm.getActiveNetworkInfo();
        if (nIfo != null && nIfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }

    }


}
