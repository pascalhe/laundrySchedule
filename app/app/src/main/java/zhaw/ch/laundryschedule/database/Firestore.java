package zhaw.ch.laundryschedule.database;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Firestore {

    private static Firestore instance = null;
    private FirebaseFirestore firestore;

    private Firestore(){
        firestore = FirebaseFirestore.getInstance();

        // Settings for Firestore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
    }

    public static FirebaseFirestore getInstance(){
        if(instance == null){
            instance = new Firestore();
        }
        return instance.firestore;
    }
}
