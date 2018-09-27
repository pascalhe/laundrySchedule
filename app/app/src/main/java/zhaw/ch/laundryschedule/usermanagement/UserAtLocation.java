package zhaw.ch.laundryschedule.usermanagement;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import zhaw.ch.laundryschedule.database.Firestore;
import zhaw.ch.laundryschedule.models.User;

/**
 * Holds a list of users from my location.
 * The list will be loaded after a user login.
 * The list is needed to display the user on a reservations card
 */
public class UserAtLocation {

    private static UserAtLocation instance = null;
    private List<User> userListAtLocation;

    /**
     * Returns a instance from the UserAtLocation class
     * @return
     */
    public static UserAtLocation getInstance(){
        if (instance == null)
            return null;
        return instance;
    }

    /**
     * Create a instance from the UserAtLocation class
     * @param locationDocId
     */
    public static void createInstance(String locationDocId){
        if(instance == null)
            instance = new UserAtLocation(locationDocId);
        else
            instance.setUserListAtLocation(locationDocId);
    }

    /**
     * Constuctor
     * @param locationDocId
     */
    private UserAtLocation(String locationDocId){
        userListAtLocation = new ArrayList<>();
        setUserListAtLocation(locationDocId);
    }

    /**
     * Search a user with the firestore document key
     * @param docKey
     * @return a user
     */
    public User getUser(String docKey){
        for(User user : userListAtLocation) {
            if(user.getDocumentKey().equals(docKey)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Get all users from a location and save them the userListAtLocation
     * @param locationDocId
     */
    private void setUserListAtLocation(String locationDocId){
        Query query = Firestore.getInstance().collection("users").whereEqualTo("locationDocId", locationDocId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        final User user = document.toObject(User.class);
                        user.setDocumentKey(document.getId());
                        userListAtLocation.add(user);
                    }
                } else {
                    Log.w("Error", "Error getting documents.", task.getException());
                }
            }
        });
    }
}
