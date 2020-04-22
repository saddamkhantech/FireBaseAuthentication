package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout emailid,passwordid;
    private Button loginid,registerid;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        hideprogressbar();


    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUi();
    }

    public void loginUser(View view)
    {
        if(!validateemail() | !validatePassword())
        {
            return;
        }
        //now start process for login
        String email=emailid.getEditText().getText().toString().trim();
        String password=passwordid.getEditText().getText().toString().trim();
        showProgressbar();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"Login Succesfully",Toast.LENGTH_LONG).show();
                            updateUi();
                            hideprogressbar();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                            hideprogressbar();
                        }
                    }
                });

    }

    private void updateUi() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null)
        {
            Toast.makeText(MainActivity.this,"User not login",Toast.LENGTH_LONG).show();
            displayTextView.setText("No User");

        }
        else {
            displayTextView.setText(firebaseUser.getEmail());

        }
    }

    public void createUser(View view)
    {
        if(!validateemail() | !validatePassword())
        {
            return;
        }
        String email=emailid.getEditText().getText().toString().trim();
        String password=passwordid.getEditText().getText().toString().trim();
        showProgressbar();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"User Created",Toast.LENGTH_LONG).show();
                            updateUi();
                            hideprogressbar();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"User Creation is failed",Toast.LENGTH_LONG).show();
                            hideprogressbar();
                        }
                    }
                });

    }
    public void signout(View view)
    {
        firebaseAuth.signOut();
        updateUi();
    }

    private boolean validatePassword() {
        String password=passwordid.getEditText().getText().toString().trim();
        if(password.isEmpty())
        {
            passwordid.setError("Password is required");
            return false;
        }
        else if (password.length()<6)
        {
            passwordid.setError("Minium lenght should be 6");
            return false;
        }
        else {
            passwordid.setError(null);
            return true;
        }
    }

    private boolean validateemail() {
        String email=emailid.getEditText().getText().toString().trim();
        if(email.isEmpty())
        {
            emailid.setError("Email is required");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailid.setError("invalid Email");
            return false;
        }
        else {
            emailid.setError(null);
            return true;
        }
    }


    private void hideprogressbar(){progressBar.setVisibility(View.GONE);}
    private void showProgressbar(){
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initialize() {
        displayTextView=findViewById(R.id.displayTextView);
        emailid=findViewById(R.id.emailId);
        passwordid=findViewById(R.id.passwordid);
        loginid=findViewById(R.id.loginbutton);
        registerid=findViewById(R.id.createbutton);
        progressBar=findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();



    }
}
