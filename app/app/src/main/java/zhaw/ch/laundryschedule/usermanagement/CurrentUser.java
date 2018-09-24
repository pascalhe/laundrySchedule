package zhaw.ch.laundryschedule.usermanagement;

import zhaw.ch.laundryschedule.models.User;

/**
 * Represents the current user.
 * The user is set after login
 */
public class CurrentUser {

    private static CurrentUser instance = null;
    private User user; // Current user

    /** Constructor
     * @param user The current user.
     */
    private CurrentUser(User user){
        this.user = user;
    }

    /**
     * Returns the current user
     * @return User object
     */
    public User getUser(){
        return user;
    }

    /**
     * Create a singleton user instance
     * @param user the current user
     */
    public static void createInstance(User user){
        if(instance == null)
            instance = new CurrentUser(user);
        else
            instance.user = user;
    }

    /**
     * return a instance from the currentUser class
     * @return currentUser
     */
    public static CurrentUser getInstance(){
        if(instance == null)
            return null;
        return instance;
    }
}
