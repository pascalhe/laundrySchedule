package zhaw.ch.laundryschedule.machines;

import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class MachineListTest {
    @Test
    public void getInstanceTestNull(){
        assertEquals(null, MachineList.getInstance());
    }
}
