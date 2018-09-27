package zhaw.ch.laundryschedule.locations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.UUID;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.Location;

public class LocationActivity extends AppCompatActivity {

    private Button saveLocationButton;
    private String documentKey;
    private EditText street;
    private EditText streetNumber;
    private EditText zipCode;
    private EditText city;
    private EditText country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get EditTexts
        street = findViewById(R.id.street);
        streetNumber = findViewById(R.id.streetNumber);
        zipCode = findViewById(R.id.zipCode);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);

        // Save or add location
        saveLocationButton = findViewById(R.id.saveLocation);
        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocation();
            }
        });

        // Check intent
        Intent locationIntent = getIntent();
        if (locationIntent.hasExtra("documentKey")) {
            // Override button text from add to update
            saveLocationButton.setText(R.string.update_location);

            documentKey = locationIntent.getStringExtra("documentKey");
            DocumentReference docRef = Firestore.getInstance().collection("locations").document(documentKey);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Location location = documentSnapshot.toObject(Location.class);
                    if (location != null)
                        setLocationInForm(location);
                }
            });
        }
    }

    private void saveLocation() {
        Location location = getLocationFromForm();

        // If documentKey empty, generate a new key
        if (documentKey == null || documentKey.isEmpty())
            documentKey = UUID.randomUUID().toString().replace("-", "");

        Firestore.getInstance().collection("locations").document(documentKey).set(location);
        Intent lSMainActivityIntent = new Intent(getBaseContext(), LSMainActivity.class);
        lSMainActivityIntent.putExtra("menuId", R.id.nav_locations);
        startActivity(lSMainActivityIntent);
    }

    private void setLocationInForm(Location location) {
        street.setText(location.getStreet());
        streetNumber.setText(location.getStreetNumber());
        zipCode.setText(Integer.toString(location.getZipCode()));
        city.setText(location.getCity());
        country.setText(location.getCountry());
    }

    private Location getLocationFromForm() {
        return new Location(
                street.getText().toString(),
                streetNumber.getText().toString(),
                Integer.parseInt(zipCode.getText().toString()),
                city.getText().toString(),
                country.getText().toString()
        );
    }

}
