package db;

import org.junit.Test;

public class FloatItemTester {
    @Test
    public void FloatTest() {
        TableItem t = new FloatItem((float) 3.560145010363);
        System.out.println(t.toString());
        TableItem t2 = new FloatItem((float) 3.1);
        System.out.println(t2.toString());
        TableItem t3 = new FloatItem((float) 3);
        System.out.println(t3.toString());
    }


}
