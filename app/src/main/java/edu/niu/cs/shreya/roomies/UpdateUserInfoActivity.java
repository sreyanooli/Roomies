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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    ConstraintLayout constraintLayout;

    private ProgressDialog progressDialog;

    private EditText nameTxt, phoneNumTxt;
    private Button updateBtn;
    private TextInputLayout nameWrapper, phoneNumWrapper;
    private TextView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("roomies");

        nameTxt = (EditText) findViewById(R.id.nameTxt);

        nameTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        phoneNumTxt = (EditText) findViewById(R.id.phoneNumTxt);

        phoneNumTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        phoneNumWrapper = (TextInputLayout) findViewById(R.id.phoneNumWrapper);

        updateBtn = (Button) findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                updateUser();
            }
        });

        backBtn = (TextView) findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateUserInfoActivity.this, UserHomeActivity.class));
                finish();
            }
        });


    }

    private void updateUser() {

        final String name = nameTxt.getText().toString().trim();
        String phoneNumber = phoneNumTxt.getText().toString().trim();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build();



        String userId = user.getUid();
        String email = user.getEmail();

        Roomie roomie = new Roomie(name, phoneNumber, email);

        myRef.child(userId).setValue(roomie);

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                        }
                    }
                });

        Snackbar snackbar = Snackbar.make(constraintLayout, "User information updated", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateUserInfoActivity.this, UserHomeActivity.class));
                finish();
            }
        });
        snackbar.show();

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
}
