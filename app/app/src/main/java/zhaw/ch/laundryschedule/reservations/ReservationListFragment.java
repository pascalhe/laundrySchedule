package zhaw.ch.laundryschedule.reservations;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.machines.MachineList;
import zhaw.ch.laundryschedule.models.Reservation;
import zhaw.ch.laundryschedule.models.WashingMachine;

public class ReservationListFragment extends Fragment {

    private List<Reservation> reservationList = new ArrayList<>();
    private Button addReservation;

    public static ReservationListFragment newInstance() {
        ReservationListFragment fragment = new ReservationListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInst) {

        // set the fragment layout.
        View rootV = inflater.inflate(R.layout.content_reservation_list, container, false);

        // Add add reservation button
        addReservation = (Button) rootV.findViewById(R.id.addReservationButton);
        addReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), ReservationActivity.class);
                startActivity(intent);
            }
        });

        // Get only reservations from current user location
        CollectionReference citiesRef = Firestore.getInstance().collection("reservations");
        //for (WashingMachine machine : MachineList.getInstance().getMachineList()){
        //    citiesRef.whereEqualTo("washingMachineDocId", machine.getDocumentKey());
        //}

        citiesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        final Reservation reservation = document.toObject(Reservation.class);
                        reservation.setDocumentKey(document.getId());
                        reservationList.add(reservation);
                    }
                    Context ctx = getActivity().getApplicationContext();

                    RecyclerView rv = (RecyclerView)getView().findViewById(R.id.reservation_recycler_view);

                    rv.setHasFixedSize(true);

                    LinearLayoutManager llm = new LinearLayoutManager(ctx);
                    rv.setLayoutManager(llm);

                    ReservationRecyclerViewAdapter adapter = new ReservationRecyclerViewAdapter(reservationList);
                    rv.setAdapter(adapter);
                } else {
                    Log.w("Error", "Error getting documents.", task.getException());
                }
            }
        });

        return rootV;
    }
}
