package db;

import org.junit.Test;

public class BinarySelectTester {
    @Test
    public void testMultiSelect() {
        Database db = new Database();
        db.transact("load examples/records");
        System.out.println(db.transact("select * from examples/records where Wins > Losses and Season > Ties"));
    }

    @Test
    public void testNOVALUE() {
        Database db = new Database();
        db.transact("load examples/teams");
        System.out.println(db.transact("select TeamName + Stadium as q from examples/teams"));
    }
    @Test
    public void TestNOVALUEWhere() {
        Database db = new Database();
        db.transact("load examples/teams");
        System.out.println(db.transact("select * from examples/teams where TeamName > Stadium"));
    }

    @Test
    public void GradescopePussy() {
        Database db = new Database();
        db.transact("load examples/teams");
        db.transact("load examples/fans");
        System.out.println(db.transact("create table t1 as select Firstname,Lastname,Mascot from examples/fans,examples/teams where Mascot <= 'Pat Patriot' and Lastname < 'Ray'"));
        System.out.println(db.transact("print t1"));
    }

    @Test
    public void NaNmyPussy() {
        Database db = new Database();
        db.transact("create table t1 (x int, y float)");
        db.transact("insert into t1 values 5, NOVALUE");
        db.transact("insert into t1 values 1123, NOVALUE");
        System.out.println(db.transact("create table s as select x, x / y as q from t1"));
        System.out.println(db.transact("select * from s where x < q"));
    }
}
