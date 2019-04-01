package db;

import org.junit.Test;
import java.util.ArrayList;

public class TableReaderTester {
    @Test
    public void readerTest() {
        String[][] a = new String[2][];
        TableReader r = new TableReader("examples/t1.tbl");
        a[0] = r.nextRow();
        a[1] = r.nextRow();
        for (String s : a[0]) {
            System.out.println(s);
        }
        for (String s : a[1]) {
            System.out.println(s);
        }
    }
}
