package zhaw.ch.laundryschedule.usermanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.User;

/**
 * 
 */
public class UserListFragment extends Fragment {

    private List<User> userList = new ArrayList<>();
    private Button addUserButton;

    public static UserListFragment newInstance(){
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInst) {

        // set the fragment layout.
        final View rootV = inflater.inflate(R.layout.content_user_list, container, false);

        // Get all users end send it to the listview
        Firestore.getInstance().collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                final User user = document.toObject(User.class);
                                user.setDocumentKey(document.getId());
                                userList.add(user);
                            }

                            Context ctx = getActivity().getApplicationContext();
                            ListView listView = (ListView)getView().findViewById(R.id.user_list);
                            UserListViewAdapter adapter = new UserListViewAdapter(userList, ctx);
                            listView.setAdapter(adapter);

                            // Set an item click listener for ListView
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Get the selected item text from ListView
                                    User selectedItem = (User) parent.getItemAtPosition(position);
                                    Intent userIntent = new Intent(getActivity().getBaseContext(), UserActivity.class);
                                    userIntent.putExtra("documentKey", selectedItem.getDocumentKey());
                                    startActivity(userIntent);
                                }
                            });
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });

        return rootV;
    }
}
