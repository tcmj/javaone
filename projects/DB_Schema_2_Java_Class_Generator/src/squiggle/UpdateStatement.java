/*
 * UpdateStatement.java
 * Created:  31. Oktober 2005, 03:10
 * Modified:
 */

package squiggle;

import com.truemesh.squiggle.Column;
import com.truemesh.squiggle.Criteria;
import com.truemesh.squiggle.JoinCriteria;
import com.truemesh.squiggle.MatchCriteria;
import com.truemesh.squiggle.Order;
import com.truemesh.squiggle.Table;
import com.truemesh.squiggle.output.Output;
import com.truemesh.squiggle.output.Outputable;
import com.truemesh.squiggle.output.ToStringer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tdeut - Thomas Deutsch - tcmj
 */
public class UpdateStatement  implements Outputable{
    
    /** Version Info */
    private static final String CLASSVERSION = "[1.0] [squiggle.UpdateStatement] [31. Oktober 2005]";
    
    public static final int indentSize = 4;
    
    private Table baseTable;
    private List columns;
    private List criteria;
    private List order;
    
    
    
    
    /** Konstruktor */
    public UpdateStatement(Table baseTable) {
        this.baseTable = baseTable;
        columns = new ArrayList();
        criteria = new ArrayList();
        order = new ArrayList();
    }
    
    /** Methode
     */
    private void me() throws Exception{
        
    }
    
    
    /**
     * Gibt Informationen der Klasse zurueck.
     * @return Version Datum und Klassenname
     */
    public static final String getVersion(){
        return CLASSVERSION;
    }
    
    
    public void write(Output out) {
        
        out.println("UPDATE");
        
        // Determine all tables used in query
        out.indent();
        appendList(out, findAllUsedTables(), ",");
        out.unindent();
        out.println("( SET ");
        
        // Add columns to select
        out.indent();
        appendList(out, columns, " = ?,");
        out.unindent();
        
        // Add tables to select from
        out.println(")");
        
        
        
        // Add criteria
        if (criteria.size() > 0) {
            out.println("WHERE");
            out.indent();
            appendList(out, criteria, "AND");
            out.unindent();
        }
        
        // Add order
        if (order.size() > 0) {
            out.println("ORDER BY");
            out.indent();
            appendList(out, order, ",");
            out.unindent();
        }
        
    }
    
    
    /**
     * Iterate through a Collection and append all entries (using .toString()) to
     * a StringBuffer.
     */
    private void appendList(Output out, Collection collection, String seperator) {
        Iterator i = collection.iterator();
        boolean hasNext = i.hasNext();
        
        while (hasNext) {
            Outputable curr = (Outputable) i.next();
            hasNext = i.hasNext();
            curr.write(out);
            out.print(' ');
            if (hasNext) {
                out.print(seperator);
            }
            out.println();
        }
    }
    
    
    /**
     * Find all the tables used in the query (from columns, criteria and order).
     *
     * @return List of {@link com.truemesh.squiggle.Table}s
     */
    private List findAllUsedTables() {
        
        List allTables = new ArrayList();
        allTables.add(baseTable);
        
        { // Get all tables used by columns
            Iterator i = columns.iterator();
            while (i.hasNext()) {
                Table curr = ((Column) i.next()).getTable();
                if (!allTables.contains(curr)) {
                    allTables.add(curr);
                }
            }
        }
        
        { // Get all tables used by criteria
            Iterator i = criteria.iterator();
            while (i.hasNext()) {
                try {
                    JoinCriteria curr = (JoinCriteria) i.next();
                    if (!allTables.contains(curr.getSource().getTable())) {
                        allTables.add(curr.getSource().getTable());
                    }
                    if (!allTables.contains(curr.getDest().getTable())) {
                        allTables.add(curr.getDest().getTable());
                    }
                } catch (ClassCastException e) {
                } // not a JoinCriteria
            }
        }
        
        { // Get all tables used by columns
            Iterator i = order.iterator();
            while (i.hasNext()) {
                Order curr = (Order) i.next();
                Table c = curr.getColumn().getTable();
                if (!allTables.contains(c)) {
                    allTables.add(c);
                }
            }
        }
        
        return allTables;
    }
    
    
    
    public void addColumn(Column column) {
        columns.add(column);
    }
    
    /**
     * Syntax sugar for addColumn(Column).
     */
    public void addColumn(Table table, String columname) {
        addColumn(table.getColumn(columname));
    }
    
    public void addCriteria(Criteria criteria) {
        this.criteria.add(criteria);
    }
    public String toString() {
        return ToStringer.toString(this);
    }
    
    /** Example Start Methode.
     * @param args Kommandozeilenparameter
     */
    public static void main(String[] args) {
        try{
            Table task = new Table("task");
            UpdateStatement update = new UpdateStatement(task);
            
            
            update.addColumn(task, "task_id");
            update.addColumn(task, "task_short_name");
            
            update.addCriteria(new MatchCriteria(task, "task_short_name", MatchCriteria.EQUALS, "tdeet"));
            
            
            update.addCriteria(new MatchCriteria(task, "task_short_name", MatchCriteria.EQUALS, "dfdf"));
            
            
            System.out.println(update.toString());
            
//            app.me();
            
        }catch(Exception ex){
            System.out.println("An Error Occured: "+ex.getMessage());
        }
    }
    
}
