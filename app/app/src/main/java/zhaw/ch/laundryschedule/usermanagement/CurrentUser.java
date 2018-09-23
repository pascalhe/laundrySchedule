package zhaw.ch.laundryschedule.usermanagement;

import zhaw.ch.laundryschedule.models.User;

public class CurrentUser {

    private static CurrentUser instance = null;
    private User user;

    private CurrentUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public static void createInstance(User user){
        if(instance == null)
            instance = new CurrentUser(user);
        else
            instance.user = user;
    }

    public static CurrentUser getInstance(){
        if(instance == null)
            return null;
        return instance;
    }
}
