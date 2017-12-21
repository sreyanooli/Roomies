package edu.niu.cs.shreya.roomies;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteOnWallActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ConstraintLayout constraintLayout;
    private EditText wallContentTxt;
    private Button postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_on_wall);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts");

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        wallContentTxt = (EditText) findViewById(R.id.wallContentTxt);

        postBtn = (Button) findViewById(R.id.postBtn);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String postId = myRef.push().getKey();

                String email = user.getEmail();
                String message = wallContentTxt.getText().toString().trim();

                Post newPost = new Post(email, message);

                myRef.child(postId).setValue(newPost);

                Snackbar snackbar = Snackbar.make(constraintLayout, "Message posted to wall" , Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(WriteOnWallActivity.this, UserHomeActivity.class));
                        finish();
                    }
                });
                snackbar.show();
            }
        });

    }
}
