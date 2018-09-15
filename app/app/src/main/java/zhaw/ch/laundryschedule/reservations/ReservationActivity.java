package zhaw.ch.laundryschedule.reservations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.Reservation;
import zhaw.ch.laundryschedule.models.WashingMachine;

public class ReservationActivity extends AppCompatActivity {

    private Button saveReservationButton;
    private String documentKey;
    private DatePicker reservationDateFrom;
    private DatePicker reservationDateTo;
    private TimePicker reservationTimeFrom;
    private TimePicker reservationTimeTo;
    private WashingMachine washingMachine = null;
    private List<WashingMachine> washingMachineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get Times and Dates
        reservationDateFrom = (DatePicker) findViewById(R.id.reservationDateFrom);
        reservationDateTo = (DatePicker) findViewById(R.id.reservationDateTo);
        reservationTimeFrom = (TimePicker) findViewById(R.id.reservationTimeFrom);
        reservationTimeTo = (TimePicker) findViewById(R.id.reservationTimeTo);

        // Save or add user
        saveReservationButton = (Button) findViewById(R.id.saveReservation);
        saveReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReservation();
            }
        });

        // Check intent
        Intent reservationIntent = getIntent();
        if (reservationIntent.hasExtra("documentKey")) {
            // Override button text from add to update
            saveReservationButton.setText("Update User");

            documentKey = reservationIntent.getStringExtra("documentKey");
            DocumentReference docRef = Firestore.getInstance().collection("reservations").document(documentKey);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Reservation reservation = documentSnapshot.toObject(Reservation.class);
                    if (reservation != null){
                        setReservationInForm(reservation);
                        //LocationSpinner.setLocationSpinner((Spinner)findViewById(R.id.locationId), user.getLocationDocId(), UserActivity.this);
                    }
                }
            });
        }
    }

    /**
     * save or update a existing reservation
     */
    private void saveReservation() {
        Reservation reservation = getReservationFromForm();

        // If documentKey empty, generate a new key
        if (documentKey == null || documentKey.isEmpty())
            documentKey = UUID.randomUUID().toString().replace("-", "");

        Firestore.getInstance().collection("reservations").document(documentKey).set(reservation);
        Intent lSMainActivityIntent = new Intent(getBaseContext(), LSMainActivity.class);
        lSMainActivityIntent.putExtra("menuId", R.id.nav_usermanagement);
        startActivity(lSMainActivityIntent);
    }

    /**
     * Mapped a reservation object in the form
     *
     * @param reservation
     */
    private void setReservationInForm(Reservation reservation) {
        reservationDateFrom.updateDate(reservation.getFrom().getYear(),
                reservation.getFrom().getMonth(),
                reservation.getFrom().getDay());
        reservationTimeFrom.setHour(reservation.getFrom().getHours());
        reservationTimeFrom.setMinute(reservation.getFrom().getMinutes());

        reservationDateTo.updateDate(reservation.getTo().getYear(),
                reservation.getTo().getMonth(),
                reservation.getTo().getDay());
        reservationTimeTo.setHour(reservation.getTo().getHours());
        reservationTimeTo.setMinute(reservation.getTo().getMinutes());
    }

    /**
     * Returns a mapped reservation from the form
     *
     * @return Reservation
     */
    private Reservation getReservationFromForm() {

        return new Reservation(
                new Date(),
                new Date(),
                ""
        );
    }

}
