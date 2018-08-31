package zhaw.ch.laundryschedule.usermanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.models.User;

public class UserListActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Firestore
        firestore = FirebaseFirestore.getInstance();

        // Settings for Firestore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        // receive all users from firestore and map to objects
        firestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                userList.add(user);
                            }

                            //and set the adapter for thiss list view
                            Context ctx = getApplicationContext();
                            ListView listView = (ListView)findViewById(R.id.user_list);
                            UserListViewAdapter adapter = new UserListViewAdapter(userList, ctx);
                            listView.setAdapter(adapter);
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });
/*
        // Listview
        userList.add(new User("Jon", "Quadroni", "jon.quadroni@klzh.ch", "quadroni", "geheim", null));
        userList.add(new User("Simone", "Schwyter", "simone.schwyter@hotmail.ch", "simsi", "geheim", null));
        userList.add(new User("Sara", "Quadroni", "sara@sw-photoart.ch", "sara", "geheim", null));
        userList.add(new User("Phillipe", "Correia", "correia@tcp.ch", "quadroni", "geheim", null));

        //and set the adapter for thiss list view
        Context ctx = getApplicationContext();
        ListView listView = (ListView)findViewById(R.id.user_list);
        UserListViewAdapter adapter = new UserListViewAdapter(userList, ctx);
        listView.setAdapter(adapter);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_usermanagement, menu);
        return true;
    }

}
