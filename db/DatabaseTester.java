package db;

import org.junit.Test;

public class DatabaseTester {
    @Test
    public void testBasic() {
        Database db = new Database();
        System.out.println(db.transact("create table t2 (a notype)"));
    }
    @Test
    public void testCreate() {
        Database db = new Database();
        db.transact("create table t1 (x string, y       string)");
    }

    @Test
    public void testLoad() {
        Database db = new Database();
        db.transact("load examples/t1");
    }

    @Test
    public void testShittyLoad() {
        Database db = new Database();
        System.out.println(db.transact("load zerkjzlk"));
    }

    @Test
    public void testShittierLoad() {
        Database db = new Database();
        System.out.println(db.transact("load t1"));
    }

    @Test
    public void testShittyInsert() {
        Database db = new Database();
        System.out.println(db.transact("insert into duck values 1, 2"));
    }

    @Test public void testJoin() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("create table t2 (x int, z int)");
        db.transact("insert into t2 values 1, 3");
        System.out.println(db.transact("select * from t1, t2"));
    }

    @Test public void testPrintLoad() {
        Database db = new Database();
        db.transact("load examples/t1");
        System.out.println(db.transact("print examples/t1"));
    }

    @Test public void testPrintCreate() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int, z string)");
        db.transact("insert into t1 values 1, 2, 'fucktruck'");
        System.out.println(db.transact("print t1"));
    }

    @Test
    public void testStore() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 1, 3");
        db.transact("store t1");
    }

    @Test
    public void testMultiJoin() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x int, z int)");
        db.transact("insert into t2 values 1, 3");
        db.transact("insert into t2 values 2, 5");
        db.transact("create table t3 (x int, w int)");
        db.transact("insert into t3 values 1, 69");
        db.transact("insert into t3 values 2, 520");
        System.out.println(db.transact("select * from t1, t2, t3"));
    }

    @Test
    public void testCreateSelect() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x int, z int)");
        db.transact("insert into t2 values 1, 3");
        db.transact("insert into t2 values 2, 5");
        db.transact("create table t3 (x int, w int)");
        db.transact("insert into t3 values 1, 69");
        db.transact("insert into t3 values 2, 520");
        db.transact("create table t4 as select * from t1, t2, t3");
        System.out.println(db.transact("print t4"));
    }

    @Test
    public void testSelectCols() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x int, z int)");
        db.transact("insert into t2 values 1, 3");
        db.transact("insert into t2 values 2, 5");
        db.transact("create table t3 (x int, w int)");
        db.transact("insert into t3 values 1, 69");
        db.transact("insert into t3 values 2, 520");
        db.transact("create table t4 as select x, w from t1, t2, t3");
        System.out.println(db.transact("print t4"));
    }

    @Test
    public void testSelectWithSpaces() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x int, z int)");
        db.transact("insert into t2 values 1, 3");
        db.transact("insert into t2 values 2, 5");
        db.transact("create table t3 (x int, w int)");
        db.transact("insert into t3 values 1, 69");
        db.transact("insert into t3 values 2, 520");
        db.transact("create table t4 as select x      ,    w from t1, t2, t3");
        System.out.println(db.transact("print t4"));
    }

    @Test
    public void testFancySelect() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x int, z int)");
        db.transact("insert into t2 values 1, 3");
        db.transact("insert into t2 values 2, 5");
        db.transact("create table t3 (x int, w int)");
        db.transact("insert into t3 values 1, 69");
        db.transact("insert into t3 values 2, 520");
        db.transact("create table t4 as select x, w, x+w as q from t1, t2, t3");
        System.out.println(db.transact("print t4"));
    }
    @Test
    public void testFloatRounding() {
        Database db = new Database();
        db.transact("create table t1 (x float, y float)");
        db.transact("insert into t1 values 99.999, 99.9999");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x float, z float)");
        db.transact("insert into t2 values 99.999, 99.999999");
        db.transact("insert into t2 values 2, 5");
        System.out.println(db.transact("select * from t1, t2"));
    }
    @Test
    public void testDrop() {
        Database db = new Database();
        db.transact("create table t1 (x float, y float)");
        db.transact("insert into t1 values 99.999, 99.9999");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x float, z float)");
        db.transact("insert into t2 values 99.999, 99.999999");
        db.transact("insert into t2 values 2, 5");
        db.transact("drop table t1");
    }

    @Test
    public void testWhere() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 2, 3");
        db.transact("create table t2 (x int, z int)");
        db.transact("insert into t2 values 1, 3");
        db.transact("insert into t2 values 2, 5");
        db.transact("select * from t1, t2 where x > y");
    }

    @Test
    public void testStringCombine() {
        Database db = new Database();
        db.transact("create table t1 (x string, y string)");
        db.transact("insert into t1 values 'cat', 'dog'");
        System.out.println(db.transact("select x + y as q from t1"));
    }

    @Test
    public void testBinaryCombine() {
        Database db = new Database();
        System.out.println(db.transact("load examples/records"));
        System.out.println(db.transact("select TeamName,Season,Wins,Losses from examples/records where Wins > Losses"));
    }

    @Test
    public void testNOVALUEInsert() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, NOVALUE");
        System.out.println(db.transact("print t1"));
    }

    @Test
    public void testNOVALUESelect() {
        Database db = new Database();
        db.transact("create table t1 (x int, y int)");
        db.transact("insert into t1 values 1, 2");
        db.transact("insert into t1 values 1, NOVALUE");
        db.transact("insert into t1 values 2, NOVALUE");
        System.out.println(db.transact("select x + y as q from t1"));
    }
}
