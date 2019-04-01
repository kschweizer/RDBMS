package db;

import java.util.HashMap;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {

    /** tables in this database are stored by this HashMap */
    private HashMap<String, Table> tables;

    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+",
            SPACE = "\\s* \\s*";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*"
            + "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+"
                    + "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                    + "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+"
                    + "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+"
                    + SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?"
                    + "\\s*(?:,\\s*.+?\\s*)*)");

    public Database() {
        this.tables = new HashMap<>();
    }

    public String transact(String query) {
        Matcher m;
        try {
            if ((m = CREATE_CMD.matcher(query)).matches()) {
                return createTable(m.group(1));
            } else if ((m = LOAD_CMD.matcher(query)).matches()) {
                return loadTable(m.group(1));
            } else if ((m = STORE_CMD.matcher(query)).matches()) {
                return storeTable(m.group(1));
            } else if ((m = DROP_CMD.matcher(query)).matches()) {
                return dropTable(m.group(1));
            } else if ((m = INSERT_CMD.matcher(query)).matches()) {
                return insertRow(m.group(1));
            } else if ((m = PRINT_CMD.matcher(query)).matches()) {
                return printTable(m.group(1));
            } else if ((m = SELECT_CMD.matcher(query)).matches()) {
                return select(m.group(1));
            } else {
                return "ERROR: Malformed query: " + query;
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
    private String createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            return createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            throw new IllegalArgumentException("ERROR: Malformed create: " + expr);
        }
    }

    private String createNewTable(String name, String[] cols) {
        for (int i = 0; i < cols.length; i += 1) {
            cols[i] = cols[i].replaceAll(SPACE, " ");
        }
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < cols.length; i++) {
            joiner.add(cols[i]);
        }

        String columnPrint = joiner.toString();
        try {
            tables.put(name, new Table(cols, columnPrint));
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return "";
    }

    private String createSelectedTable(String name, String exprs, String joining, String conds) {
        tables.put(name, select(exprs, joining, conds));
        return "";
    }

    private String loadTable(String name) {
        if (!TableReader.exists(name)) {
            return "ERROR: table does not exist";
        }
        TableReader r = new TableReader(name + ".tbl");
        String columns = r.getColumns();
        try {
            transact("create table " + name + " (" + columns + ")");
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        Table toLoad = tables.get(name);
        if (toLoad == null) {
            return "ERROR: .*";
        }
        try {
            while (r.hasRow()) {
                String[] row = r.nextRow();
                toLoad.addStringRow(row);
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return "";
    }

    private String storeTable(String name) {
        if (!tables.containsKey(name)) {
            return "ERROR: No such table: " + name;
        }
        return TableReader.write(tables.get(name), name);
    }

    private String dropTable(String name) {
        if (!tables.containsKey(name)) {
            return "ERROR: no such table: " + name;
        }
        tables.remove(name);
        return "";
    }

    private String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed insert: " + expr;
        }
        String tableName = m.group(1);
        String rowInsert = m.group(2);
        Table currentTable = tables.get(tableName);
        String[] rows = rowInsert.split(COMMA);
        if (currentTable == null) {
            return "ERROR: .*";
        }
        try {
            currentTable.addStringRow(rows);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return "";
    }

    private String printTable(String name) {
        if (!tables.containsKey(name)) {
            return "ERROR: No such table: " + name;
        }
        Table currentTable = tables.get(name);
        return currentTable.printTable();
    }

    private String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed select: " + expr;
        }
        String exprs = m.group(1);
        String tablesJoining = m.group(2);
        String conds = m.group(3);
        String[] oldTables = tablesJoining.split(COMMA);
        for (String s : oldTables) {
            if (!tables.containsKey(s)) {
                return "ERROR: No such table: " + s;
            }
        }
        return select(m.group(1), m.group(2), m.group(3)).printTable();
    }

    private String[] condsToArray(String conds) {
        if (conds == null) {
            return null;
        }
        return conds.split(AND);
    }

    private Table select(String exprs, String tablesJoining, String conds) {
        String[] oldTables = tablesJoining.split(COMMA);
        String[] selection = exprs.split(COMMA);
        String[] condArray = condsToArray(conds);
        Table temp = tables.get(oldTables[0]);
        for (int i = 1; i < oldTables.length; i += 1) {
            temp = Table.join(temp, tables.get(oldTables[i]));
        }
        Table finalSelect;
        if (exprs.equals("*")) {
            finalSelect = temp;
        } else {
            if (selection[0].split((SPACE)).length > 1) {
                finalSelect = temp.newSelect(selection[0]);
            } else {
                finalSelect = temp.select(selection[0]);
            }
            for (int i = 1; i < selection.length; i += 1) {
                if (selection[i].split(SPACE).length > 1) {
                    finalSelect = Table.combine(finalSelect,
                            temp.newSelect(selection[i]));
                } else {
                    finalSelect = Table.combine(finalSelect, temp.select(selection[i]));
                }
            }
        }
        if (!(conds == null)) {
            finalSelect = finalSelect.filter(condArray);
        }
        return finalSelect;
    }
}
