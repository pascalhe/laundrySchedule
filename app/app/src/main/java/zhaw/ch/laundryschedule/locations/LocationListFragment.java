package zhaw.ch.laundryschedule.locations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import zhaw.ch.laundryschedule.models.Location;

public class LocationListFragment extends Fragment {

    private List<Location> locationList = new ArrayList<>();
    private Button addLocationButton;

    public static LocationListFragment newInstance() {
        LocationListFragment fragment = new LocationListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInst) {
        // set the fragment layout.
        View rootV = inflater.inflate(R.layout.content_location_list, container, false);

        // Add location button
        addLocationButton = (Button) rootV.findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), LocationActivity.class);
                startActivity(intent);
            }
        });

        // Get all users end send it to the listview
        Firestore.getInstance().collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                final Location location = document.toObject(Location.class);
                                location.setDocumentKey(document.getId());
                                locationList.add(location);
                            }
                            Context ctx = getActivity().getApplicationContext();

                            ListView listView = (ListView) getView().findViewById(R.id.location_list);

                            LocationListViewAdapter adapter = new LocationListViewAdapter(locationList, ctx);
                            listView.setAdapter(adapter);

                            // Set an item click listener for ListView
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Get the selected item text from ListView
                                    Location selectedItem = (Location) parent.getItemAtPosition(position);
                                    Intent locationIntent = new Intent(getActivity().getBaseContext(), LocationActivity.class);
                                    locationIntent.putExtra("documentKey", selectedItem.getDocumentKey());
                                    startActivity(locationIntent);
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
