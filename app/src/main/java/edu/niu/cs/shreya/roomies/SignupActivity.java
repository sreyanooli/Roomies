package edu.niu.cs.shreya.roomies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    ConstraintLayout constraintLayout;

    private ProgressDialog progressDialog;

    Button registerBtn;
    EditText usernameTxt, passwordTxt;
    TextView loginPageBtn;
    TextInputLayout usernameWrapper, passwordWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        //Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Init firebase database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering roomie....");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);

        usernameTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        passwordTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        registerBtn = (Button) findViewById(R.id.resetPwdBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                String username = usernameTxt.getText().toString().trim();
                String password = passwordTxt.getText().toString().trim();

                if(!validateEmail(username)) {
                    usernameWrapper.setError("Not a valid email!");
                }
                else if(!validatePassword(password)) {
                    passwordWrapper.setError("Choose a password atleast 6 characters long!");
                }
                else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    progressDialog.show();
                    registerRoomie(username, password);
                }
            }
        });

        loginPageBtn = (TextView) findViewById(R.id.backBtn);

        loginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void registerRoomie(String username, String password) {

        firebaseAuth.createUserWithEmailAndPassword(username, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Snackbar snackbar = Snackbar.make(constraintLayout, "Error: " + task.getException()
                            , Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                        else {

                            progressDialog.dismiss();
                            goToHome();
                        }
                    }
                });

    }

    private void goToHome()
    {
        Intent goToHomeIntent = new Intent(SignupActivity.this, UserHomeActivity.class);
        startActivity(goToHomeIntent);
        finish();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();

        if(view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }
}
