package db;
import edu.princeton.cs.algs4.In;
import java.io.File;
import java.io.PrintWriter;

public class TableReader {
    private static final String COMMA = "\\s*,\\s*";

    String filename;
    In rows;

    public static boolean exists(String filename) {
        File f = new File("tables/" + filename + ".tbl");
        return f.exists();
    }
    public TableReader(String filename) {
        this.filename = filename;
        this.rows = new In(filename);
        rows.readLine();
    }

    public String getColumns() {
        In in = new In(filename);
        return in.readLine();
    }

    public boolean hasRow() {
        return !rows.isEmpty();
    }

    public String[] nextRow() {
        String row = rows.readLine();
        return row.split(COMMA);
    }

    public static String write(Table t, String name) {
        try{
            PrintWriter writer = new PrintWriter("tables/" + name + ".tbl", "UTF-8");
            writer.println(t.getColumnPrint());
            for (Object r : t) {
                Row tr = (Row) r;
                writer.print(tr.printRow());
            }
            writer.close();
        } catch (Exception e) {
            return "";
        }
        return "";
    }
}
