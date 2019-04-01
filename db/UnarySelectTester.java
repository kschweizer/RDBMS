package db;

import org.junit.Test;

public class UnarySelectTester {
    @Test
    public void testBasicSelect() {
        Database db = new Database();
        db.transact("load examples/records");
        System.out.println(db.transact("select * from examples/records where Wins > 8"));
    }
}
