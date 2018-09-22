package zhaw.ch.laundryschedule.machines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.WashingMachine;

public class MachineSpinner {
    private static List<WashingMachine> machineList;
    private static SpinMachineAdapter spinMachineAdapter;
    private static WashingMachine selectedMachine;

    public static WashingMachine getSelectedMachine(){
        return selectedMachine;
    }

    public static String getMachineReference(){
        String docKey = selectedMachine.getDocumentKey();
        if(docKey != null || docKey.isEmpty())
            return docKey;
        else
            return null;
    }

    public static void setMachineSpinner(final Spinner spinner, final String machineDocId, final Context context){
        machineList = new ArrayList<WashingMachine>();
        Firestore.getInstance().collection("machines")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        setMachineSpinner(task.getResult(), spinner, machineDocId, context);
                    } else {
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                }
            });
    }

    public static void setMachineSpinnerByLocation(final Spinner spinner, final String machineDocId, final String locationDocId, final Context context){
        machineList = new ArrayList<WashingMachine>();
        Query query = Firestore.getInstance().collection("machines").whereEqualTo("locationDocId", locationDocId);;
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        setMachineSpinner(task.getResult(), spinner, machineDocId, context);
                    } else {
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                }
            });
    }

    private static void setMachineSpinner(QuerySnapshot querySnapshot, final Spinner spinner, final String machineDocId, final Context context){
        machineList = new ArrayList<WashingMachine>();

        for (DocumentSnapshot document : querySnapshot) {
            final WashingMachine machine = document.toObject(WashingMachine.class);
            machine.setDocumentKey(document.getId());
            if (machine.getName() != null)
                machineList.add(machine);
            if (machine.getDocumentKey().equals(machineDocId))
                selectedMachine = machine;
        }
        WashingMachine[] machineArr = new WashingMachine[machineList.size()];
        machineArr = machineList.toArray(machineArr);
        // Creating adapter for spinner
        spinMachineAdapter = new SpinMachineAdapter(context,
                android.R.layout.simple_spinner_item, machineArr);
        // attaching data adapter to spinner
        spinner.setAdapter(spinMachineAdapter);

        // set machine
        if (machineDocId != null && !machineDocId.isEmpty()) {
            spinner.setSelection(machineList.indexOf(selectedMachine));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedMachine = spinMachineAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

}
