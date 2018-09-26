package zhaw.ch.laundryschedule.usermanagement;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserAtLocationTest {

    @Test
    public void getInstanceTestNull(){
        assertEquals(null, UserAtLocation.getInstance());
    }

}
