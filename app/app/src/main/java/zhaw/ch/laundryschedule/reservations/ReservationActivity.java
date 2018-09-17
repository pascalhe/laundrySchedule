package zhaw.ch.laundryschedule.reservations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.machines.MachineSpinner;
import zhaw.ch.laundryschedule.models.Reservation;
import zhaw.ch.laundryschedule.models.WashingMachine;

public class ReservationActivity extends AppCompatActivity {

    private Button saveReservationButton;
    private String documentKey;
    private EditText reservationDateFrom;
    private EditText reservationTimeFrom;
    private EditText reservationTimeTo;
    private WashingMachine washingMachine = null;
    private List<WashingMachine> washingMachineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get Times and Dates
        reservationDateFrom = (EditText) findViewById(R.id.reservationDate);
        reservationTimeFrom = (EditText) findViewById(R.id.reservationTimeFrom);
        reservationTimeTo = (EditText) findViewById(R.id.reservationTimeTo);

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
                        MachineSpinner.setMachineSpinner((Spinner) findViewById(R.id.washingMachineId), null, ReservationActivity.this);
                    }
                }
            });
        } else {
            MachineSpinner.setMachineSpinner((Spinner) findViewById(R.id.washingMachineId), null, ReservationActivity.this);
        }

        // Date picker
        reservationDateFrom.setClickable(true);
        reservationDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setEditBox(reservationDateFrom);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        // Time picker from
        reservationTimeFrom.setClickable(true);
        reservationTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setEditBox(reservationTimeFrom);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        // Time picker to
        reservationTimeTo.setClickable(true);
        reservationTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setEditBox(reservationTimeTo);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
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
        lSMainActivityIntent.putExtra("menuId", R.id.nav_reservation);
        startActivity(lSMainActivityIntent);
    }

    /**
     * Mapped a reservation object in the form
     *
     * @param reservation
     */
    private void setReservationInForm(Reservation reservation) {
        /*
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
        */
    }

    /**
     * Returns a mapped reservation from the form
     *
     * @return Reservation
     */
    private Reservation getReservationFromForm() {
        Date from;
        Date to;
        Date timeFrom;
        Date timeTo;

        String date_var=(reservationDateFrom.getText().toString());
        String timeFrom_var =(reservationTimeFrom.getText().toString());
        String timeTo_var =(reservationTimeTo.getText().toString());
        DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat timeFormatter = new SimpleDateFormat("HH:ss");

        try {
            timeFrom = timeFormatter.parse(timeFrom_var);
            timeTo = timeFormatter.parse(timeTo_var);
            from = dateFormatter.parse(date_var);
            to = dateFormatter.parse(date_var);

            from.setHours(timeFrom.getHours());
            from.setMinutes(timeFrom.getMinutes());
            to.setHours(timeTo.getHours());
            to.setMinutes(timeTo.getMinutes());

            return new Reservation(
                    from,
                    to,
                    ""
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
