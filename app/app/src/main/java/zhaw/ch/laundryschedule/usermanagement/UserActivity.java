package zhaw.ch.laundryschedule.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.locations.LocationSpinner;
import zhaw.ch.laundryschedule.locations.SpinLocationAdapter;
import zhaw.ch.laundryschedule.models.Location;
import zhaw.ch.laundryschedule.models.User;

public class UserActivity extends AppCompatActivity{

    private Button saveUserButton;
    private String documentKey;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Location userLocation = null;
    private List<Location> locationList = new ArrayList<Location>();
    private SpinLocationAdapter spinLocationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get EditTexts
        firstNameEditText = (EditText) findViewById(R.id.firstName);
        lastNameEditText = (EditText) findViewById(R.id.lastName);
        emailEditText = (EditText) findViewById(R.id.email);
        userNameEditText = (EditText) findViewById(R.id.userName);
        passwordEditText = (EditText) findViewById(R.id.password);

        // Save or add user
        saveUserButton = (Button) findViewById(R.id.saveUser);
        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });

        // Check intent
        Intent userIntent = getIntent();
        if (userIntent.hasExtra("documentKey")) {
            // Override button text from add to update
            saveUserButton.setText("Update User");

            documentKey = userIntent.getStringExtra("documentKey");
            DocumentReference docRef = Firestore.getInstance().collection("users").document(documentKey);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null){
                    setUserInForm(user); // set user in form
                    LocationSpinner.setLocationSpinner((Spinner)findViewById(R.id.locationId), user.getLocationDocId(), UserActivity.this);
                }
                }
            });
        }
    }

    /**
     * save or update a existing user
     */
    private void saveUser() {
        User user = getUserFromForm();

        // If documentKey empty, generate a new key
        if (documentKey == null || documentKey.isEmpty())
            documentKey = UUID.randomUUID().toString().replace("-", "");

        Firestore.getInstance().collection("users").document(documentKey).set(user);
        Intent lSMainActivityIntent = new Intent(getBaseContext(), LSMainActivity.class);
        lSMainActivityIntent.putExtra("menuId", R.id.nav_usermanagement);
        startActivity(lSMainActivityIntent);
    }

    /**
     * Mapped a user object in the form
     *
     * @param user
     */
    private void setUserInForm(User user) {
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        emailEditText.setText(user.getEmail());
        userNameEditText.setText(user.getUserName());
        passwordEditText.setText("Password");
    }

    /**
     * Returns a mapped user from the form
     *
     * @return User
     */
    private User getUserFromForm() {
        return new User(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                emailEditText.getText().toString(),
                userNameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                LocationSpinner.getLocationReference()
        );
    }
}
