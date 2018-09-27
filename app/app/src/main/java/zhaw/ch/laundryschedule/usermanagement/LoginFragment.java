package zhaw.ch.laundryschedule.usermanagement;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.util.ProgressView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    // Firebase
    private FirebaseAuth mAuth;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private View rootV;


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInst) {

        // set the fragment layout.
        rootV = inflater.inflate(R.layout.fragment_login, container, false);
        // Set up the login form.
        mLoginFormView = rootV.findViewById(R.id.login_form);
        mProgressView = rootV.findViewById(R.id.login_progress);
        mEmailView = rootV.findViewById(R.id.email);
        mPasswordView = rootV.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = rootV.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        return rootV;
    }


    /**
     * Checks if form entries are valid and either proceeds with the login process
     * or shows the errors
     */
    private void attemptLogin() {

        // Init fields/variables
        mEmailView.setError(null);
        mPasswordView.setError(null);
        boolean cancel = false;
        View focusView = null;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!LoginValidation.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!LoginValidation.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            signIn(email, password);
        }
    }


    /**
     * sign in with email and password
     * checks against user info in firebase
     *
     * @param email    String valid email address
     * @param password String valid password
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showProgress(false);
                            // Sign in success
                            Log.d(TAG, "signInWithEmail:success");
                            Snackbar.make(rootV, "Login successful. ", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            Intent lSMainActivityIntent = new Intent(getContext(), LSMainActivity.class);
                            lSMainActivityIntent.putExtra("menuId", R.id.nav_reservation);
                            startActivity(lSMainActivityIntent);
                        } else {
                            // Sign in fails
                            showProgress(false);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Snackbar.make(rootV, "Authentication failed. " + Objects.requireNonNull(task.getException()).getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
    }


    private void showProgress(Boolean show){
        ProgressView.show(show, mLoginFormView, mProgressView, Objects.requireNonNull(getContext()));
    }


}

