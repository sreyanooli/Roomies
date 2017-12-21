package edu.niu.cs.shreya.roomies;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {

    Button signoutBtn, viewWallBtn, writeWallBtn, updateInfoBtn;
    TextView welcomeTxt;

    private FirebaseAuth firebaseAuth;

    String dispName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        firebaseAuth = FirebaseAuth.getInstance();

        welcomeTxt = (TextView) findViewById(R.id.welcomeTxt);

        dispName = "Welcome, " + firebaseAuth.getCurrentUser().getDisplayName();

        welcomeTxt.setText(dispName);

        updateInfoBtn = (Button) findViewById(R.id.updateInfoBtn);
        writeWallBtn = (Button) findViewById(R.id.writeWallBtn);
        viewWallBtn = (Button) findViewById(R.id.viewWallBtn);
        signoutBtn = (Button) findViewById(R.id.signOutBtn);

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, UpdateUserInfoActivity.class));
                finish();
            }
        });

        writeWallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, WriteOnWallActivity.class));
            }
        });

        viewWallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, ViewWallActivity.class));
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
            }
        });
    }
}
