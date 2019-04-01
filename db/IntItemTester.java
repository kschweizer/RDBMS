package db;
import org.junit.Test;

public class IntItemTester {
    @Test
    public void addTester() {
        TableItem t1 = new IntItem(5);
        TableItem t2 = new FloatItem((float) 5.6);
        TableItem t3 = t1.add(t2);
    }
}
