package zhaw.ch.laundryschedule.machines;

import android.support.annotation.NonNull;
import android.util.Log;

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

public class MachineList {

    private static MachineList instance = null;
    private List<WashingMachine> machineList;

    public static MachineList getInstance(){
        if(instance == null)
            return null;
        return instance;
    }

    public static void createInstance(String locationDocId){
        if(instance == null)
            instance = new MachineList(locationDocId);
        else
            instance.setMachineList(locationDocId);
    }

    private MachineList(String locationDocId){
        machineList = new ArrayList<>();
        setMachineList(locationDocId);
    }

    public WashingMachine getMachine(String docKey){
        for(WashingMachine machine : machineList) {
            if(machine.getDocumentKey().equals(docKey)) {
                return machine;
            }
        }
        return null;
    }

    public List<WashingMachine> getMachineList(){
        return machineList;
    }

    private void setMachineList(String locationDocId){
        Query query = Firestore.getInstance().collection("machines").whereEqualTo("locationDocId", locationDocId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                final WashingMachine machine = document.toObject(WashingMachine.class);
                                machine.setDocumentKey(document.getId());
                                machineList.add(machine);
                            }
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
