/*
 * SquiggleTest.java
 * Created:  31. Oktober 2005, 02:57
 * Modified:
 */

package squiggle;

import com.truemesh.squiggle.SelectQuery;
import com.truemesh.squiggle.Table;

/**
 *
 * @author tdeut - Thomas Deutsch - tcmj
 */
public class SquiggleTest {
    
    /** Version Info */
    private static final String CLASSVERSION = "[1.0] [squiggle.SquiggleTest] [31. Oktober 2005]";
    
    /** Konstruktor */
    public SquiggleTest() {
    }
    
    /** Methode
     */
    private void me() throws Exception{
        Table task = new Table("task");
        SelectQuery select = new SelectQuery(task);
        
        select.addColumn(task, "task_id");
        select.addColumn(task, "task_short_name");
        
        System.out.println(select.toString());
        
        
    }
    
    
    /**
     * Gibt Informationen der Klasse zurueck.
     * @return Version Datum und Klassenname
     */
    public static final String getVersion(){
        return CLASSVERSION;
    }
    
    
    /** Example Start Methode.
     * @param args Kommandozeilenparameter
     */
    public static void main(String[] args) {
        try{
            
            SquiggleTest app = new SquiggleTest();
            app.me();
            
        }catch(Exception ex){
            System.out.println("An Error Occured: "+ex.getMessage());
        }
    }
    
}
