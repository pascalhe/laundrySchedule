package zhaw.ch.laundryschedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import zhaw.ch.laundryschedule.locations.LocationListFragment;
import zhaw.ch.laundryschedule.machines.MachineListFragment;
import zhaw.ch.laundryschedule.reservations.ReservationListFragment;
import zhaw.ch.laundryschedule.service.UploadService;
import zhaw.ch.laundryschedule.usermanagement.LoginFragment;
import zhaw.ch.laundryschedule.usermanagement.UserListFragment;

public class LSMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "LSMainActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth mAuth;
    private ImageView profilePicture;
    private TextView userName;
    private TextView userEmail;

    private String picturePath;
    private Uri mFileUri = null;
    private Uri mDownloadUrl = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsmain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userEmail = headerView.findViewById(R.id.user_email);
        userName = headerView.findViewById(R.id.user_name);
        profilePicture = headerView.findViewById(R.id.profile_picture);




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Check intent
        Intent intent = getIntent();
        setFragment(intent.getIntExtra("menuId", R.id.nav_reservation));
    }

    private void setProfilePicture() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
            profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "launchCamera");
                    dispatchTakePictureIntent();
                }
            });
        } else {
            //setFragment(R.id.nav_login);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profilePicture.setImageBitmap(imageBitmap);

                mFileUri = data.getData();

                if (mFileUri != null) {
                    uploadFromUri(mFileUri);
                } else {
                    Log.w(TAG, "File URI is null");
                }
            }
        }


    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Clear the last download, if any
        updateUI(mAuth.getCurrentUser());
        mDownloadUrl = null;

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, UploadService.class)
                .putExtra(UploadService.EXTRA_FILE_URI, fileUri)
                .setAction(UploadService.ACTION_UPLOAD));

        // Show loading spinner

    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.d(TAG, "updateUI: "+ currentUser.getEmail()+" currentemail "+userEmail.getText());
            if (currentUser.getEmail() != null) {
                userEmail.setText(currentUser.getEmail());
            }
            if (currentUser.getDisplayName() != null){
                userName.setText(currentUser.getDisplayName());
            }
            if (currentUser.getPhotoUrl() != null && !currentUser.getPhotoUrl().toString().isEmpty()) {
                StorageReference profilePictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentUser.getPhotoUrl().toString());


                File localFile = null;
                try {
                    localFile = File.createTempFile("profilePictures", "jpg");
                    picturePath = localFile.getPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                profilePictureReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(picturePath);
                        profilePicture.setImageBitmap(myBitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

                break;
            case R.id.nav_gallery:

                break;
            case R.id.nav_login:
                LoginFragment loginFragment = LoginFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, loginFragment, "login");
                break;
            case R.id.nav_usermanagement:
                UserListFragment userListFragment = UserListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, userListFragment, "user");
                break;
            case R.id.nav_washing_machines:
                MachineListFragment machineListFragment = MachineListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, machineListFragment, "machine");
                break;
            case R.id.nav_locations:
                LocationListFragment locationListFragment = LocationListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, locationListFragment, "location");
                break;
        }
        fragmentTransaction.commit();
    }
}
