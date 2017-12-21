package edu.niu.cs.shreya.roomies;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewWallActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference postRef, userRef;

    private ConstraintLayout constraintLayout;
    private Button backToHomeBtn;
    private TextView wallContentsTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wall);

        database = FirebaseDatabase.getInstance();
        postRef = database.getReference();
        userRef = database.getReference();

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        wallContentsTxt = (TextView) findViewById(R.id.wallContentTxt);

        wallContentsTxt.setText("");

        wallContentsTxt.setMovementMethod(new ScrollingMovementMethod());

        postRef.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showPosts(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar snackbar = Snackbar.make(constraintLayout, "Read failed!" , Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();
            }
        });

        backToHomeBtn = (Button) findViewById(R.id.backToHomeBtn);

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewWallActivity.this, UserHomeActivity.class));
                finish();
            }
        });

    }

    private void showPosts(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.getChildren()) {
            Post readPost = ds.getValue(Post.class);
            wallContentsTxt.append(readPost.toString());
        }
    }
}
