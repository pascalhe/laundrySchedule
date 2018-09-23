package zhaw.ch.laundryschedule.reservations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.machines.MachineList;
import zhaw.ch.laundryschedule.models.Reservation;
import zhaw.ch.laundryschedule.usermanagement.CurrentUser;
import zhaw.ch.laundryschedule.usermanagement.UserAtLocation;

public class ReservationRecyclerViewAdapter extends RecyclerView.Adapter<ReservationRecyclerViewAdapter.ReservationViewHolder>{

    private List<Reservation> reservationList;
    private Context context;

    public ReservationRecyclerViewAdapter(List<Reservation> reservationList, Context context){
        this.reservationList = reservationList;
        this.context = context;

    }

    private void removeCard(int i){
        reservationList.remove(i);
        this.notifyItemChanged(i);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_reservation_list, viewGroup, false);
        ReservationViewHolder pvh = new ReservationViewHolder(v);

        pvh.reservationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicl", "Bla");
            }
        });


        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReservationViewHolder reservationViewHolder, final int i) {
        SimpleDateFormat fDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat fTime = new SimpleDateFormat("HH:mm");
        String machineDocKey = reservationList.get(i).getWashingMachineDocId();
        final String userDocKey = reservationList.get(i).getUserDocId();

        reservationViewHolder.date.setText(fDate.format(reservationList.get(i).getFrom()));
        reservationViewHolder.from.setText("Time: " + fTime.format(reservationList.get(i).getFrom()) + " - " + fTime.format(reservationList.get(i).getTo()));
        reservationViewHolder.machine.setText("Machine: " + MachineList.getInstance().getMachine(machineDocKey).getName());
        reservationViewHolder.user.setText("User: " + UserAtLocation.getInstance().getUser(userDocKey).getUserName());

        reservationViewHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, reservationViewHolder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.reservation_card_options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_reservation:
                                if(CurrentUser.getInstance().getUser().getUserName().equals(
                                        UserAtLocation.getInstance().getUser(userDocKey).getUserName())){
                                    Firestore.getInstance().collection("reservations").document(reservationList.get(i).getDocumentKey())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    removeCard(i);
                                                    Toast toast = Toast.makeText(context, "Reservation deleted", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast toast = Toast.makeText(context, "Reservation not deleted", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            });
                                }else{
                                    Toast toast = Toast.makeText(context, "Not my reservation. Deleting not possible!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
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
        TextView machine;
        TextView user;
        TextView buttonViewOption;

        ReservationViewHolder(View itemView) {
            super(itemView);

            reservationCard = (CardView)itemView.findViewById(R.id.reservation_card_view);
            date = (TextView)itemView.findViewById(R.id.reservation_card_date);
            from = (TextView)itemView.findViewById(R.id.reservation_card_time);
            machine = (TextView)itemView.findViewById(R.id.reservation_card_machine);
            user = (TextView)itemView.findViewById(R.id.reservation_card_user);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }

}

