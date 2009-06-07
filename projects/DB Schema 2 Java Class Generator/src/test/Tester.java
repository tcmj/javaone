/*
 * Tester.java
 * Created:  31. August 2005, 00:30
 * Modified:
 */

package test;

/**
 *
 * @author tdeut - Thomas Deutsch - tcmj
 */
public class Tester{
    
    /** Version Info */
    private static final String CLASSVERSION = "[1.0] [test.Tester] [31. August 2005]";
    
    /** Konstruktor */
    public Tester() {
    }
    
    private  String s = "a";
    
    private static final boolean debug = true;
    
    
    public interface A{
        public static final boolean debug = true;
        public static final String S = "a";
    }
    
    
    private static final String TAB = "   ";
    
    
    private void me() throws Exception{
        
        StringBuffer b = new StringBuffer();
        int lo = 4;
        while(lo<99){
            b.append(TAB+TAB+TAB+TAB+"Hallo");
            lo++;
        }
        
        
        
        long oo = time();
        
        
        
        App app = new App();
        
        int loop = 10000000;
        
        for(int i=0;i<loop;i++){
        
            String x = s+i;
//          if(i % 100000 == 0){
//              System.out.println("Free Memory: "+Runtime.getRuntime().freeMemory()+"   "+i);
//          }
          
        }
        
        long ooo = time();
        
        System.out.println("Time: "+(ooo-oo)/1000D+" sec.");
         
        
        if(debug){
            System.out.println("Debugmode ON");
        }
        
        for(int i=0;i<3;i++){
            String x = new String("x").intern();
            System.out.println("X: "+System.identityHashCode(x));
        }
        
         
        
        
    }
    
    
    
    
    private static final long time(){
        return System.currentTimeMillis();
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
            
            Tester app = new Tester();
            app.me();
            
        }catch(Exception ex){
            System.out.println("An Error Occured: "+ex.getMessage());
        }
        System.exit(0);
    }

    /**
     * Holds value of property batch_sum_flag.
     */
    private String batch_sum_flag;

    /**
     * Getter for property batch_sum_flag.
     * @return Value of property batch_sum_flag.
     */
    public String getBatch_sum_flag() {

        return this.batch_sum_flag;
    }

    /**
     * Setter for property batch_sum_flag.
     * @param batch_sum_flag New value of property batch_sum_flag.
     */
    public void setBatch_sum_flag(String batch_sum_flag) {

        this.batch_sum_flag = batch_sum_flag;
    }
    
}
