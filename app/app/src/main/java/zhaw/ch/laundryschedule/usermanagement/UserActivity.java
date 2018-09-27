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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;
import java.util.UUID;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.locations.LocationSpinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        // Check intent
        Intent userIntent = getIntent();
        if (userIntent.hasExtra("documentKey")) {
            // Override button text from add to update
            saveUserButton.setText(R.string.update_user);

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
            focusView.requestFocus();
        } else {
            showProgress(true);
            writeUserToDb(user);
        }
    }


    /**
     * Writes the User Object to the Firebase database
     *
     * @param user User
     */
    private void writeUserToDb(User user) {
        if (documentKey == null || documentKey.isEmpty())
            documentKey = UUID.randomUUID().toString().replace("-", "");

        Firestore.getInstance().collection("users").document(documentKey).set(user)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            showProgress(false);
                            Snackbar.make(formView, R.string.user_success, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            startIntent();
                        } else {
                            showProgress(false);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "writeUserToDb:failure", task.getException());
                            Snackbar.make(formView, "Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
    }


    /**
     * Starts Intent of LSMain Activity
     */
    private void startIntent(){
        Intent lSMainActivityIntent = new Intent(getBaseContext(), LSMainActivity.class);
        lSMainActivityIntent.putExtra("menuId",R.id.nav_usermanagement);
        startActivity(lSMainActivityIntent);
    }


    /**
     * Shows a ProgressView
     *
     * @param show Boolean
     */
    private void showProgress(Boolean show) {
        ProgressView.show(show, formView, progressView, getBaseContext());
    }

    /**
     * Mapped a user object in the form
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
