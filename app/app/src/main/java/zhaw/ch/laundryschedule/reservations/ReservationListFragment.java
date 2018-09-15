package zhaw.ch.laundryschedule.reservations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import zhaw.ch.laundryschedule.R;

public class ReservationListFragment extends Fragment {

    private Button addReservationButton;

    public static ReservationListFragment newInstance(){
        ReservationListFragment fragment = new ReservationListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInst) {
        // set the fragment layout.
        View rootV = inflater.inflate(R.layout.content_reservation_list, container, false);

        addReservationButton = (Button)rootV.findViewById(R.id.addReservationButton);
        addReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), ReservationActivity.class);
                startActivity(intent);
            }
        });

        return rootV;
    }
}
