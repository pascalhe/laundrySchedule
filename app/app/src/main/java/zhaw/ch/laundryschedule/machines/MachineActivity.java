package zhaw.ch.laundryschedule.machines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.UUID;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.locations.LocationSpinner;
import zhaw.ch.laundryschedule.models.WashingMachine;

public class MachineActivity extends AppCompatActivity {

    private Button saveMachineButton;
    private String documentKey;
    private EditText name;
    private EditText capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get EditTexts
        name = (EditText) findViewById(R.id.machineNameEdit);
        capacity = (EditText) findViewById(R.id.machineCapacityEdit);

        // Save or add location
        saveMachineButton = (Button) findViewById(R.id.saveMachine);
        saveMachineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMachine();
            }
        });

        // Check intent
        Intent machineIntent = getIntent();
        if (machineIntent.hasExtra("documentKey")) {
            // Override button text from add to update
            saveMachineButton.setText("Update Machine");

            documentKey = machineIntent.getStringExtra("documentKey");
            DocumentReference docRef = Firestore.getInstance().collection("machines").document(documentKey);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    WashingMachine machine = documentSnapshot.toObject(WashingMachine.class);
                    if(machine != null){
                        setMachineInForm(machine);
                        LocationSpinner.setLocationSpinner((Spinner)findViewById(R.id.locationId), machine.getLocationDocId(), MachineActivity.this);
                    }
                }
            });
        }
    }

    private void saveMachine() {
        WashingMachine machine = getMachineFromForm();

        // If documentKey empty, generate a new key
        if (documentKey == null || documentKey.isEmpty())
            documentKey = UUID.randomUUID().toString().replace("-", "");

        Firestore.getInstance().collection("machines").document(documentKey).set(machine);
        Intent lSMainActivityIntent = new Intent(getBaseContext(), LSMainActivity.class);
        lSMainActivityIntent.putExtra("menuId", R.id.nav_washing_machines);
        startActivity(lSMainActivityIntent);
    }

    private void setMachineInForm(WashingMachine machine) {
        name.setText(machine.getName());
        capacity.setText(machine.getCapacity());
    }

    private WashingMachine getMachineFromForm() {
        return new WashingMachine(
                name.getText().toString(),
                capacity.getText().toString(),
                LocationSpinner.getLocationReference()
        );
    }
}