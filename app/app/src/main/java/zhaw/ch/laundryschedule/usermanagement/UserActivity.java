package zhaw.ch.laundryschedule.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.locations.LocationSpinner;
import zhaw.ch.laundryschedule.locations.SpinLocationAdapter;
import zhaw.ch.laundryschedule.models.Location;
import zhaw.ch.laundryschedule.models.User;
import zhaw.ch.laundryschedule.util.ProgressView;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";

    private Button saveUserButton;
    private String documentKey;
    private View progressView;
    private View formView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Location userLocation = null;
    private List<Location> locationList = new ArrayList<Location>();
    private SpinLocationAdapter spinLocationAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressView = findViewById(R.id.user_progress);
        formView = findViewById(R.id.user_form);

        // get EditTexts
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        userNameEditText = findViewById(R.id.userName);
        passwordEditText = findViewById(R.id.password);

        // Save or add user
        saveUserButton = findViewById(R.id.saveUser);
        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });

        mAuth = FirebaseAuth.getInstance();

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
                    if (user != null) {
                        setUserInForm(user); // set user in form
                        LocationSpinner.setLocationSpinner((Spinner) findViewById(R.id.locationId), user.getLocationDocId(), UserActivity.this);
                    }
                }
            });
        } else{
            LocationSpinner.setLocationSpinner((Spinner)findViewById(R.id.locationId), null, UserActivity.this);
        }
    }

    /**
     * save or update a existing user
     */
    private void saveUser() {
        final User user = getUserFromForm();
        // Reset errors.
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the create attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !LoginValidation.isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!LoginValidation.isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt create and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user create user attempt.
            showProgress(true);
            // If documentKey empty, create a new user
            if (documentKey == null || documentKey.isEmpty()) {
                createUser(email, password, user);
            } else {
                writeUserToDb(user);
            }

        }
    }


    private void createUser(String email, String password, final User user) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            documentKey = task.getResult().getUser().getUid();
                            Log.d(TAG, "onComplete: documentKey " + documentKey);
                            Log.d(TAG, "onComplete: currentUser "+mAuth.getCurrentUser().getUid());
                            writeUserToDb(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Snackbar.make(getCurrentFocus(), "Failed: " + task.getException().getMessage(), Snackbar.LENGTH_LONG)
//                                    .setAction("Action", null).show();
                        }
                    }
                });
    }


    private void writeUserToDb(User user) {
        Log.d(TAG, "writeUserToDb: " +documentKey+ user.toString());

        Firestore.getInstance().collection("users").document(documentKey).set(user)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            showProgress(false);
                            startIntent();
                        } else {
                            showProgress(false);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "writeUserToDb:failure", task.getException());
                            Snackbar.make(getCurrentFocus(), "Failed: " + task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }
                });
    }
    private void startIntent(){
        Intent lSMainActivityIntent = new Intent(getBaseContext(), LSMainActivity.class);
        lSMainActivityIntent.putExtra("menuId",R.id.nav_usermanagement);
        startActivity(lSMainActivityIntent);
    }


    private void showProgress(Boolean show) {
        ProgressView.show(show, formView, progressView, getBaseContext());
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
        emailEditText.setFocusable(false);
        userNameEditText.setText(user.getUserName());
        passwordEditText.setVisibility(View.GONE);
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
                LocationSpinner.getLocationReference()
        );
    }
}
