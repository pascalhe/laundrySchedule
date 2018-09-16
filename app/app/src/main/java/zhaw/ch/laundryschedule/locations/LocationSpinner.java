package zhaw.ch.laundryschedule.locations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.Location;

public class LocationSpinner {

    private static List<Location> locationList;
    private static SpinLocationAdapter spinLocationAdapter;
    private static Location selectedLocation;

    public static Location getSelectedLocation() {
        return selectedLocation;
    }

    public static String getLocationReference() {
        String docKey = selectedLocation.getDocumentKey();
        if (docKey != null || !docKey.isEmpty())
            return docKey;
        else
            return null;
    }

    public static void setLocationSpinner(final Spinner spinner, final String locationDocId, final Context context) {
        // Initial locationList
        locationList = new ArrayList<Location>();
        // Get all locations for the spinner
        Firestore.getInstance().collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                final Location location = document.toObject(Location.class);
                                location.setDocumentKey(document.getId());
                                if (location.getStreet() != null)
                                    locationList.add(location);
                                if (location.getDocumentKey().equals(locationDocId))
                                    selectedLocation = location;
                            }
                            Location[] locationArr = new Location[locationList.size()];
                            locationArr = locationList.toArray(locationArr);
                            // Creating adapter for spinner
                            spinLocationAdapter = new SpinLocationAdapter(context,
                                    android.R.layout.simple_spinner_item, locationArr);
                            // attaching data adapter to spinner
                            spinner.setAdapter(spinLocationAdapter);

                            // set location
                            if (locationDocId != null && !locationDocId.isEmpty()) {
                                spinner.setSelection(locationList.indexOf(selectedLocation));
                            }

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                    selectedLocation = spinLocationAdapter.getItem(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapter) {
                                }
                            });
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
