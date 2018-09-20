package zhaw.ch.laundryschedule.reservations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.models.Reservation;

public class ReservationListViewAdapter extends BaseAdapter {

    private List<Reservation> reservationList;
    private Context context;

    public ReservationListViewAdapter(List<Reservation> reservationList, Context context){
        this.reservationList = reservationList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reservationList.size();
    }

    @Override
    public Object getItem(int i) {
        return reservationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return reservationList.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            vi = inflater.inflate(R.layout.cell_location_list, null);
        }

        TextView reservation_card_from = vi.findViewById(R.id.reservation_card_from);
        TextView reservation_card_to = vi.findViewById(R.id.reservation_card_to);

        Reservation reservation = reservationList.get(i);

        //reservation_card_from.setText(reservation.getFrom().toString());
        //reservation_card_to.setText(reservation.getTo().toString());

        return vi;
    }

}
