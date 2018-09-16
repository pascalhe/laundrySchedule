package zhaw.ch.laundryschedule;

import android.content.Intent;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import zhaw.ch.laundryschedule.locations.LocationListFragment;
import zhaw.ch.laundryschedule.machines.MachineListFragment;
import zhaw.ch.laundryschedule.reservations.ReservationListFragment;
import zhaw.ch.laundryschedule.usermanagement.LoginFragment;
import zhaw.ch.laundryschedule.usermanagement.UserListFragment;

public class LSMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsmain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check intent
        Intent intent = getIntent();
        setFragment(intent.getIntExtra("menuId", R.id.nav_reservation));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //updateUI(currentUser);
        } else {
            setFragment(R.id.nav_login);
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
        switch (id){
            case R.id.nav_reservation:
                ReservationListFragment reservationListFragment = ReservationListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, reservationListFragment,"reservation");

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
