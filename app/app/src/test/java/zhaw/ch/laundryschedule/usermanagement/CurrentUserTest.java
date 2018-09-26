package zhaw.ch.laundryschedule.usermanagement;

import org.junit.Test;

import zhaw.ch.laundryschedule.models.User;

import static org.junit.Assert.assertEquals;

public class CurrentUserTest {

    @Test
    public void getInstanceTest(){
        User user = new User("Hans", "Muster", "hans.muster@domain.ch", "HaMus", "abcdefghijklmnopqrstuvwxyz");
        CurrentUser.createInstance(user);
        assertEquals("Hans", CurrentUser.getInstance().getUser().getFirstName());
    }

    @Test
    public void getInstanceTestNull(){
        assertEquals(null, CurrentUser.getInstance());
    }

}
