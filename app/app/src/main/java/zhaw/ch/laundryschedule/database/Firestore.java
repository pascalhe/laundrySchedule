package zhaw.ch.laundryschedule.database;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * Returns a singleton instance from the class Firestore.
 * The class includs a firestore instance
 */
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

    /**
     * Returns a singleton instance from the class Firestore
     * @return
     */
    public static FirebaseFirestore getInstance(){
        if(instance == null){
            instance = new Firestore();
        }
        return instance.firestore;
    }
}
