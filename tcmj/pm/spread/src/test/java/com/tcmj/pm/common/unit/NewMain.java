package com.tcmj.pm.common.unit;
import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.*;
/**
 * NewMain.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 09.02.2011
 */
public class NewMain {

    /**
     * default no-arg-constructor.
     */
    public NewMain() {
    }


    /** runit. */
    public void runit() throws Exception {


        List lst = mock(List.class);
        lst.add("test");

        verify(lst).add("test");

        when(lst.get(3)).thenReturn(new Date());

        System.out.println("lst: "+lst.get(3));

    }


    /**
     * Start entry point.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            System.out.println("Starting NewMain");
            NewMain app = new NewMain();
            long lnstart = System.currentTimeMillis();
            app.runit();
            long lnend = System.currentTimeMillis();
            System.out.println("Finished NewMain in "+(lnend-lnstart)+" ms.");
        }catch (Exception exc) {
            System.out.println("Error: " + exc.getMessage());
            exc.printStackTrace();
        }

    }

}
