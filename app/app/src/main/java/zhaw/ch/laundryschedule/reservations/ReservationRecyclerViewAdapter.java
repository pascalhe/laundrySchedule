package zhaw.ch.laundryschedule.reservations;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.models.Reservation;

public class ReservationRecyclerViewAdapter extends RecyclerView.Adapter<ReservationRecyclerViewAdapter.ReservationViewHolder>{

    private List<Reservation> reservationList;

    public ReservationRecyclerViewAdapter(List<Reservation> reservationList){
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_reservation_list, viewGroup, false);
        ReservationViewHolder pvh = new ReservationViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder reservationViewHolder, int i) {
        SimpleDateFormat fDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat fTime = new SimpleDateFormat("HH:mm");

        reservationViewHolder.date.setText(fDate.format(reservationList.get(i).getFrom()));
        reservationViewHolder.from.setText(fTime.format(reservationList.get(i).getFrom()) + " - " + fTime.format(reservationList.get(i).getTo()));
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        CardView reservationCard;
        TextView date;
        TextView from;

        ReservationViewHolder(View itemView) {
            super(itemView);

            reservationCard = (CardView)itemView.findViewById(R.id.reservation_card_view);
            date = (TextView)itemView.findViewById(R.id.reservation_card_date);
            from = (TextView)itemView.findViewById(R.id.reservation_card_time);
        }
    }

}

