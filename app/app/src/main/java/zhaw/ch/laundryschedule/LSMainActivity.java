/* code for image upload to firebase storage adapted from source com.google.firebase.quickstart.firebasestorage */
package zhaw.ch.laundryschedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.locations.LocationActivity;
import zhaw.ch.laundryschedule.locations.LocationListFragment;
import zhaw.ch.laundryschedule.machines.MachineActivity;
import zhaw.ch.laundryschedule.machines.MachineList;
import zhaw.ch.laundryschedule.machines.MachineListFragment;
import zhaw.ch.laundryschedule.models.User;
import zhaw.ch.laundryschedule.reservations.ReservationActivity;
import zhaw.ch.laundryschedule.reservations.ReservationListFragment;
import zhaw.ch.laundryschedule.service.UploadService;
import zhaw.ch.laundryschedule.timer.Timer;
import zhaw.ch.laundryschedule.usermanagement.CurrentUser;
import zhaw.ch.laundryschedule.usermanagement.LoginFragment;
import zhaw.ch.laundryschedule.usermanagement.UserActivity;
import zhaw.ch.laundryschedule.usermanagement.UserAtLocation;
import zhaw.ch.laundryschedule.usermanagement.UserListFragment;

public class LSMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "LSMainActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private ImageView profilePhoto;
    private TextView userName;
    private TextView userEmail;
    private FloatingActionButton fab;
    private NavigationView navigationView;

    private BroadcastReceiver mBroadcastReceiver;
    private String imageFilePath;
    private Uri photoURI;
    private Uri mDownloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsmain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);


        userEmail = headerView.findViewById(R.id.user_email);
        userName = headerView.findViewById(R.id.user_name);
        profilePhoto = headerView.findViewById(R.id.profile_photo);


        //Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        fab = findViewById(R.id.fab);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // BroadcastReceiver for upload to firebase
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case UploadService.UPLOAD_COMPLETED:
                            mDownloadUrl = intent.getParcelableExtra(UploadService.EXTRA_DOWNLOAD_URL);
                            setProfilePhotoUrl(mDownloadUrl);
                    }
                }
            }
        };

        // Check intent
        Intent intent = getIntent();
        setFragment(intent.getIntExtra("menuId", R.id.nav_timer));
    }

    private void setProfilePhotoUrl(Uri photoUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUrl)
                .build();

        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DocumentReference docRef = Firestore.getInstance().collection("users").document(currentUser.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        user.setDocumentKey(documentSnapshot.getId());
                        CurrentUser.createInstance(user);
                        UserAtLocation.createInstance(user.getLocationDocId());
                        MachineList.createInstance(user.getLocationDocId());
                    }
                }
            });


            profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "launchCamera");
                    dispatchTakePhotoIntent();
                }
            });
            updateUI(currentUser);
        } else {
            updateUI(null);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePhotoIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
                photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
                Log.d(TAG, "dispatchTakePhotoIntent: " + photoURI.toString());
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(takePhotoIntent,
                        REQUEST_IMAGE_CAPTURE);

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "dispatchTakePhotoIntent: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: ");
                Glide.with(this)
                        .load(photoURI)
                        .apply(RequestOptions.circleCropTransform())
                        .apply(new RequestOptions().override(192))
                        .into(profilePhoto);
                uploadFromUri();
            }
        }

    }


    private void uploadFromUri() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, UploadService.getIntentFilter());
        Intent intent = new Intent(LSMainActivity.this, UploadService.class)
                .putExtra(UploadService.EXTRA_FILE_URI, photoURI)
                .setAction(UploadService.ACTION_UPLOAD)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startService(intent);
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.d(TAG, "updateUI: " + currentUser.getEmail() + " currentemail " + userEmail.getText());
            if (currentUser.getEmail() != null) {
                userEmail.setText(currentUser.getEmail());
            }
            if (currentUser.getDisplayName() != null) {
                userName.setText(currentUser.getDisplayName());
            }
            if (currentUser.getPhotoUrl() != null && !currentUser.getPhotoUrl().toString().isEmpty()) {
                StorageReference profilePhotoReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentUser.getPhotoUrl().toString());

                File localFile = null;
                try {
                    localFile = File.createTempFile("profilePhotos", "jpg");
                    imageFilePath = localFile.getPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (localFile != null) {
                    profilePhotoReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Glide.with(getApplicationContext())
                                    .load(imageFilePath)
                                    .apply(RequestOptions.circleCropTransform())
                                    .apply(new RequestOptions().override(192))
                                    .into(profilePhoto);
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d(TAG, "onFailure: " + exception.toString());
                        }
                    });
                }
            }
            navigationView.getMenu().setGroupVisible(R.id.menu_main, true);
            navigationView.getMenu().setGroupVisible(R.id.menu_management, true);
        } else {
            Log.d(TAG, "updateUI: user null");
            userEmail.setText("");
            userName.setText("");
            profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_laundry_machine_app_icon2));
            CurrentUser.createInstance(null);
            setFragment(R.id.nav_login);
            navigationView.getMenu().setGroupVisible(R.id.menu_main, false);
            navigationView.getMenu().setGroupVisible(R.id.menu_management, false);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lsmain, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            menu.setGroupVisible(R.id.action_logged_out, false);
            menu.setGroupVisible(R.id.action_logged_in, true);
        } else {
            menu.setGroupVisible(R.id.action_logged_out, true);
            menu.setGroupVisible(R.id.action_logged_in, false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_log_out:
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
            case R.id.action_log_in:
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setFragment(item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeFragments() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void setFragment(int id) {
        removeFragments();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (id) {
            case R.id.nav_reservation:
                ReservationListFragment reservationListFragment = ReservationListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, reservationListFragment, "reservation");
                fab.show();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), ReservationActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.nav_timer:
                Timer timer = Timer.newInstance();
                fragmentTransaction.add(R.id.fragment_container, timer, "timer");
                fab.hide();
                break;
            case R.id.nav_login:
                LoginFragment loginFragment = LoginFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, loginFragment, "login");
                fab.hide();
                break;
            case R.id.nav_usermanagement:
                UserListFragment userListFragment = UserListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, userListFragment, "user");
                fab.show();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), UserActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.nav_washing_machines:
                MachineListFragment machineListFragment = MachineListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, machineListFragment, "machine");
                fab.show();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), MachineActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.nav_locations:
                LocationListFragment locationListFragment = LocationListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, locationListFragment, "location");
                fab.show();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), LocationActivity.class);
                        startActivity(intent);
                    }
                });
                break;
        }
        fragmentTransaction.commit();
    }
}
