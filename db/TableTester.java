package db;

import org.junit.Test;

public class TableTester {
    @Test
    public void ConstructorTest() {
        String[] cols = new String[]{"x int", "y int"};
        Table t1 = new Table(cols, "x int, y int");
    }

    @Test
    public void ItemTest() {
        TableItem t = new FloatItem((float) 2.5);
        System.out.println(t.get());
    }

    @Test
    public void printTest() {
        String[] cols = new String[]{"x int", "y int"};
        Table t1 = new Table(cols, "x int, y int");
        t1.addStringRow(new String[]{"1", "2"});
        System.out.println(t1.printTable());
    }

    @Test
    public void printTest2() {
        String[] cols = new String[]{"x int", "y int", "q float", "money string", "urethraPayments string"};
        Table t1 = new Table(cols, "x int, y int, q float, money string, urethraPayments string");
        t1.addStringRow(new String[]{"1", "2", "3.00", "'titties'", "'tits'"});
        t1.addStringRow(new String[]{"134", "69", "4.20", "'OuO'", "'bunnies'"});
        System.out.println(t1.printTable());
    }
}
