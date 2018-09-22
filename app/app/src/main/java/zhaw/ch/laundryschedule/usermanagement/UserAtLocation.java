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

public class UserAtLocation {

    private static UserAtLocation instance = null;
    private List<User> userListAtLocation;

    public static UserAtLocation getInstance(){
        if (instance == null)
            return null;
        return instance;
    }

    public static void createInstance(String locationDocId){
        instance = new UserAtLocation(locationDocId);
    }

    private UserAtLocation(String locationDocId){
        userListAtLocation = new ArrayList<>();
        setUserListAtLocation(locationDocId);

    }

    public User getUser(String docKey){
        for(User user : userListAtLocation) {
        if(user.getDocumentKey().equals(docKey)) {
            return user;
        }
    }
        return null;
}

    public List<User> getUserListAtLocation(){
        return userListAtLocation;
    }

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
