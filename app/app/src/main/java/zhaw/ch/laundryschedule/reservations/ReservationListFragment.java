package zhaw.ch.laundryschedule.reservations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.machines.MachineList;
import zhaw.ch.laundryschedule.models.Reservation;

/**
 * Reservation list fragment class. Reads all reservations from users location.
 * The list ist needed for the reservation list in the app
 */
public class ReservationListFragment extends Fragment {

    private List<Reservation> reservationList = new ArrayList<>();
    private Button addReservation;

    public static ReservationListFragment newInstance() {
        ReservationListFragment fragment = new ReservationListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInst) {

        // set the fragment layout.
        View rootV = inflater.inflate(R.layout.content_reservation_list, container, false);

        // Get only reservations from current user location
        CollectionReference reservationRef = Firestore.getInstance().collection("reservations");
        Query query = reservationRef.whereGreaterThan("from", new Date()).orderBy("from");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        final Reservation reservation = document.toObject(Reservation.class);

                        if (reservation != null && MachineList.getInstance() != null && MachineList.getInstance().getMachine(reservation.getWashingMachineDocId()) != null) {
                            reservation.setDocumentKey(document.getId());
                            reservationList.add(reservation);
                        }

                    }
                    Context ctx = Objects.requireNonNull(getActivity()).getApplicationContext();

                    RecyclerView rv = Objects.requireNonNull(getView()).findViewById(R.id.reservation_recycler_view);

                    rv.setHasFixedSize(true);

                    LinearLayoutManager llm = new LinearLayoutManager(ctx);
                    rv.setLayoutManager(llm);

                    ReservationRecyclerViewAdapter adapter = new ReservationRecyclerViewAdapter(reservationList, ctx);
                    rv.setAdapter(adapter);
                    } else {
                    Log.w("Error", "Error getting documents.", task.getException());
                }
            }
        });

        return rootV;
    }
}
