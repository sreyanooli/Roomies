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
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPwdActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    private FirebaseAuth firebaseAuth;

    EditText emailTxt;
    TextView backBtn;
    Button resetPwdBtn;

    TextInputLayout usernameWrapper;

    ConstraintLayout constraintLayout;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        //Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sending password to your email....");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);

        emailTxt = (EditText) findViewById(R.id.emailTxt);

        emailTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        resetPwdBtn = (Button) findViewById(R.id.resetPwdBtn);

        resetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                String email = emailTxt.getText().toString().trim();

                if(!validateEmail(email)) {
                    usernameWrapper.setError("Not a valid email!");
                }
                else {
                    usernameWrapper.setErrorEnabled(false);
                    progressDialog.show();
                    resetPasswordForUser(email);
                }
            }
        });

        backBtn = (TextView) findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPwdActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void resetPasswordForUser(final String email) {
        firebaseAuth.sendPasswordResetEmail(email).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Snackbar snackbar = Snackbar.make(constraintLayout, "Sent your password to email: "
                            + email, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                        else {
                            progressDialog.dismiss();
                            Snackbar snackbar = Snackbar.make(constraintLayout, "Failed to " +
                                    "send password", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
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
}
