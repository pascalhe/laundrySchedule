package zhaw.ch.laundryschedule.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.Location;
import zhaw.ch.laundryschedule.models.User;

public class UserActivity extends AppCompatActivity {

    private Button saveUserButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Save or add user
        saveUserButton = (Button) findViewById(R.id.saveUser);
        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        // Check intent
        Intent userIntent = getIntent();
        if(!userIntent.getStringExtra("documentKey").isEmpty()){
            Log.i("DocumentKey: ",userIntent.getStringExtra("documentKey"));
        }
    }

    private void addUser(){
        user = getUserFromForm();
        Firestore.getInstance().collection("users").document(UUID.randomUUID().toString().replace("-", "")).set(user);
        Intent userlistIntent = new Intent(getBaseContext(), UserListActivity.class);
        startActivity(userlistIntent);
    }

    private void updateUser(){

    }

    /**
     * returns the user from the form
     * @return
     */
    private User getUserFromForm(){
        EditText firstNameEditText = (EditText)findViewById(R.id.firstName);
        EditText lastNameEditText = (EditText)findViewById(R.id.lastName);
        EditText emailEditText = (EditText)findViewById(R.id.email);
        EditText userNameEditText = (EditText)findViewById(R.id.userName);
        EditText passwordEditText = (EditText)findViewById(R.id.password);

        return new User(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                emailEditText.getText().toString(),
                userNameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                new Location(),
                ""
                );
    }

}
