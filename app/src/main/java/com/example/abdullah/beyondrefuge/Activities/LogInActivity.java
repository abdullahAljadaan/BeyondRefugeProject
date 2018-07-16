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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abdullah.beyondrefuge.R;
import com.example.abdullah.beyondrefuge.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LogInActivity extends AppCompatActivity {

    AutoCompleteTextView mEmailView;
    Button btnSignUpPag;
    Button btnLogIn;
    EditText etLogInPassword;
    EditText etLogInEmail;
    private FirebaseAuth mAuthLogIn;

    private FirebaseDatabase mDatabaseLogIn;
    private DatabaseReference mDatabaseRefLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        checkingInternetConnection();
        mAuthLogIn = FirebaseAuth.getInstance();
        mDatabaseLogIn = FirebaseDatabase.getInstance();
        mDatabaseRefLogIn = mDatabaseLogIn.getReference();

        etLogInEmail = (EditText)findViewById(R.id.et_login_email);
        etLogInPassword = (EditText)findViewById(R.id.et_login_password);


        btnSignUpPag = (Button) findViewById(R. id. btn_sign__up_page);
         btnSignUpPag.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intentSignUp = new Intent(LogInActivity.this , SignUpActivity.class);
                 startActivity(intentSignUp);

             }
         });
         btnLogIn = (Button) findViewById(R.id.btn_login);
         btnLogIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               logInAccount(v);
              
             }
         });


    }

    private void logInAccount(View view) {

        String emailLogIn = etLogInEmail.getText().toString();
        String passwordLogIn = etLogInPassword.getText().toString();
        if (emailLogIn.isEmpty()){
            etLogInEmail.setError("Please enter your email!");
            etLogInEmail.requestFocus();
            return;
        }
        if (passwordLogIn.isEmpty()){
            etLogInPassword.setError("Please enter your password!");
            etLogInPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLogIn).matches()){
            etLogInPassword.setError("Please enter a valid email!");
            etLogInEmail.requestFocus();
            return;
        }
        if (passwordLogIn.length()<6){
            etLogInPassword.setError("Please enter a vaild password!");
        }


        mAuthLogIn.signInWithEmailAndPassword(emailLogIn , passwordLogIn ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
           if (task.isSuccessful()){
               DatabaseReference userControl = FirebaseDatabase.getInstance().getReference();
               Query query = userControl.child("users").orderByChild("userId");
               ValueEventListener postListener = new ValueEventListener() {

                   @Override
                   public void onDataChange( @NonNull DataSnapshot dataSnapshot) {
                       User fireBaseUser = dataSnapshot.getValue(User.class);


                       if(fireBaseUser.isTagComplete){
                           finish();
                           startActivity(new Intent(LogInActivity.this, MainActivity.class));
                       } else{
                           finish();
                           startActivity(new Intent(LogInActivity.this,HomeScreanActivity.class));
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       Toast.makeText(LogInActivity.this,"Conniction field!",Toast.LENGTH_SHORT).show();

                   }
               };
               query.addListenerForSingleValueEvent(postListener);

           } else {
               Toast.makeText(getApplicationContext() , task.getException().getMessage(),Toast.LENGTH_LONG).show();
           }
            }
        });

    }


    private void checkingInternetConnection() {
        if(isOnline()){

        }
        else{
            new AlertDialog.Builder(LogInActivity.this)
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
        ConnectivityManager mConnect = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net =mConnect.getActiveNetworkInfo();
        if (net!= null && net.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }

    }


}
