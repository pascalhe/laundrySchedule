package zhaw.ch.laundryschedule.machines;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.AbstractMachine;
import zhaw.ch.laundryschedule.models.WashingMachine;


public class MachineListFragment extends Fragment {


    private List<AbstractMachine> machineList = new ArrayList<>();
    private Button addMachineButton;

    public static MachineListFragment newInstance() {
        MachineListFragment fragment = new MachineListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInst) {

        // set the fragment layout.
        View rootV = inflater.inflate(R.layout.content_machine_list, container, false);

        // Add user button
        addMachineButton = (Button) rootV.findViewById(R.id.addMachineButton);
        addMachineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), MachineActivity.class);
                startActivity(intent);
            }
        });

        // Get all users end send it to the listview
        Firestore.getInstance().collection("machines")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                final WashingMachine machine = document.toObject(WashingMachine.class);
                                machine.setDocumentKey(document.getId());
                                machineList.add(machine);
                            }
                            Context ctx = getActivity().getApplicationContext();

                            ListView listView = (ListView) getView().findViewById(R.id.machineList);

                            MachineListViewAdapter adapter = new MachineListViewAdapter(machineList, ctx);
                            listView.setAdapter(adapter);

                            // Set an item click listener for ListView
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Get the selected item text from ListView
                                    WashingMachine selectedItem = (WashingMachine) parent.getItemAtPosition(position);
                                    Intent machineIntent = new Intent(getActivity().getBaseContext(), MachineActivity.class);
                                    machineIntent.putExtra("documentKey", selectedItem.getDocumentKey());
                                    startActivity(machineIntent);
                                }
                            });

                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });

        return rootV;
    }
}
