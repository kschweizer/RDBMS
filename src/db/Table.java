package db;
import java.util.*;

public class Table<T> implements Iterable<Row> {
    // some helpful parsing tools
    String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+",
            SPACE = "\\s* \\s*";

    // myriad of class variables for all your table needs
    private String[] headers;
    private String[] colNames;
    private String[] colTypes;
    private HashMap<String, String> colNameToType;
    private ArrayList<Row> rows = new ArrayList();
    private Integer numRows;
    private Integer numCols;
    private String columnPrint;

    public Table(String[] cols, String columnPrint) {
        try {
            this.headers = cols;
            this.colNames = new String[cols.length];
            this.colTypes = new String[cols.length];
            this.colNameToType = new HashMap<>();
            for (int i = 0; i < cols.length; i += 1) {
                String[] tempSplit = cols[i].split(SPACE);
                this.colNames[i] = tempSplit[0];
                this.colTypes[i] = tempSplit[1];
                if (!validType(colTypes[i])) {
                    throw new IllegalArgumentException("ERROR: .*");
                }
                this.colNameToType.put(colNames[i], colTypes[i]);
            }
            this.columnPrint = columnPrint;
            numRows = 0;
            numCols = cols.length;
        } catch(Exception e) {
            throw new IllegalArgumentException("ERROR: .*");
        }
    }

    private boolean validType(String s) {
        return s.equals("int") || s.equals("float") || s.equals("string");
    }

    public void addStringRow(String[] row) {
        if (row.length != numCols) {
            throw new IllegalArgumentException("ERROR: .*");
        }
        TableItem[] newItems = new TableItem[numCols];
         try {
             for (int i = 0; i < numCols; i += 1) {
                 if (row[i].equals("NOVALUE")) {
                     newItems[i] = new NOVALUEItem();
                 } else if (colTypes[i].equals("int")) {
                     newItems[i] = new IntItem(Integer.parseInt(row[i]));
                 } else if (colTypes[i].equals("float")) {
                     newItems[i] = new FloatItem(Float.parseFloat(row[i]));
                 } else if (colTypes[i].equals("string")) {
                     newItems[i] = new StringItem(row[i]);
                 }
             }
             rows.add(new Row(newItems, headers));
         } catch (Exception e) {
             throw new IllegalArgumentException("ERROR: .*");
         }
        numRows += 1;
    }

    public void addRow(Row row) {
        rows.add(row);
        numRows += 1;
    }

    public Row getRow(int i) {
        return rows.get(i);
    }

    public int size() {
        return numRows;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String[] getColTypes() {
        return colTypes;
    }

    public String getNameType(String colName) {
        return colNameToType.get(colName);
    }

    public String getColumnPrint() {
        return columnPrint;
    }

    public String printTable() {
        String toPrint = columnPrint + "\n";
        for (Row r : rows) {
            toPrint += r.printRow();
        }
        return toPrint;
    }

    public boolean contains(String columnName) {
        return this.colNameToType.containsKey(columnName);
    }

    /** iterator class for the rows of the table */
    private class rowIterator implements Iterator<Row> {
        int indexer;

        public rowIterator() {
            indexer = 0;
        }

        public boolean hasNext() {
           return (indexer < size());
        }

        public Row next() {
            Row temp = getRow(indexer);
            indexer += 1;
            return temp;
        }
    }

    @Override
    public Iterator<Row> iterator() {
        return new rowIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table<?> table = (Table<?>) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(headers, table.headers)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(colNames, table.colNames)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(colTypes, table.colTypes)) return false;
        if (colNameToType != null ? !colNameToType.equals(table.colNameToType) : table.colNameToType != null)
            return false;
        if (rows != null ? !rows.equals(table.rows) : table.rows != null) return false;
        if (numRows != null ? !numRows.equals(table.numRows) : table.numRows != null) return false;
        if (numCols != null ? !numCols.equals(table.numCols) : table.numCols != null) return false;
        return columnPrint != null ? columnPrint.equals(table.columnPrint) : table.columnPrint == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(headers);
        result = 31 * result + Arrays.hashCode(colNames);
        result = 31 * result + Arrays.hashCode(colTypes);
        result = 31 * result + (colNameToType != null ? colNameToType.hashCode() : 0);
        result = 31 * result + (rows != null ? rows.hashCode() : 0);
        result = 31 * result + (numRows != null ? numRows.hashCode() : 0);
        result = 31 * result + (numCols != null ? numCols.hashCode() : 0);
        result = 31 * result + (columnPrint != null ? columnPrint.hashCode() : 0);
        return result;
    }

    /** helper method for getting a Set for the new headers of a join between two tables */
    private static ArrayList<String> newHeaders(String[] a, String[] b) {
        ArrayList<String> headerSet = new ArrayList<>();
        for (String s : a) {
            if (!headerSet.contains(s)) {
                headerSet.add(s);
            }
        }
        for (String s : b) {
            if (!headerSet.contains(s)) {
                headerSet.add(s);
            }
        }
        return headerSet;
    }

    public static Table headerJoin(Table a, Table b) {
        ArrayList<String> headerSet = Table.newHeaders(a.getHeaders(), b.getHeaders());
        String[] newHeaders = headerSet.toArray(new String[headerSet.size()]);
        StringJoiner joiner = new StringJoiner(",");
        for (String s : newHeaders) {
            joiner.add(s);
        }
        String columnPrint = joiner.toString();
        Table newTable = new Table(newHeaders, columnPrint);
        return newTable;
    }

    public static void rowJoiner(Table newTable, Table a, Table b, String[] headers) {
        for (Object r1 : a) {
            for (Object r2 : b) {
                if (Row.canJoin((Row) r1, (Row) r2, headers)) {
                    newTable.addRow(Row.join((Row) r1, (Row) r2, headers));
                }
            }
        }
    }

    public static void rowCombiner(Table newTable, Table a, Table b, String[] headers) {
        for (int i = 0; i < a.size(); i += 1) {
            Row aRow = a.getRow(i);
            Row bRow = b.getRow(i);
            TableItem[] toAdd = new TableItem[aRow.size() + bRow.size()];
            for (int j = 0; j < aRow.size(); j += 1) {
                toAdd[j] = aRow.get(j);
            }
            toAdd[aRow.size()] = bRow.get(0);
            newTable.addRow(new Row(toAdd, headers));
        }
    }

    public static Table join(Table a, Table b) {
        Table newTable = Table.headerJoin(a, b);
        Table.rowJoiner(newTable, a, b, newTable.getHeaders());
        return newTable;
    }

    /** ASSUMES INPUT IS OF THE SAME SIZE */
    public static Table combine(Table a, Table b) {
        Table temp = Table.headerJoin(a, b);
        Table.rowCombiner(temp, a, b, temp.getHeaders());
        return temp;
    }

    public Table select(String selection) {
        String columnPrint = selection + " " + getNameType(selection);
        String[] colName = new String[]{columnPrint};

        Table temp = new Table(colName, columnPrint);
        for (Row r : rows) {
            TableItem[] toAdd = new TableItem[1];
            toAdd[0] = r.get(columnPrint);
            temp.addRow(new Row(toAdd, colName));
        }
        return temp;
    }

    public Table newSelect(String selected) {
        try {
            String operator;
            String[] preSpaceSelect;
            if (selected.contains("+")) {
                operator = "+";
                preSpaceSelect = selected.split("[+]");
            } else if (selected.contains("-")) {
                operator = "-";
                preSpaceSelect = selected.split("[-]");
            } else if (selected.contains("*")) {
                operator = "*";
                preSpaceSelect = selected.split("[*]");
            } else {
                operator = "/";
                preSpaceSelect = selected.split("[/]");
            }
            StringJoiner joiner = new StringJoiner(" ");
            for (int i = 0; i < preSpaceSelect.length; i++) {
                joiner.add(preSpaceSelect[i]);
            }
            String[] selection = joiner.toString().split(SPACE);
            String col1 = selection[0] + " " + getNameType(selection[0]);
            String col2 = selection[1] + " " + getNameType(selection[1]);
            String[] newColHeader = {selection[3] + " " + overlap(selection[0], selection[1])};
            Table operatedCol = new Table(newColHeader, newColHeader[0]);
            for (int i = 0; i < size(); i++) {
                TableItem[] newVal = new TableItem[1];
                if (operator.equals("+")) {
                    newVal[0] = getRow(i).get(col1).add(getRow(i).get(col2));
                }
                if (operator.equals("-")) {
                    newVal[0] = getRow(i).get(col1).sub(getRow(i).get(col2));
                }
                if (operator.equals("*")) {
                    newVal[0] = getRow(i).get(col1).mul(getRow(i).get(col2));
                }
                if (operator.equals("/")) {
                    newVal[0] = getRow(i).get(col1).div(getRow(i).get(col2));
                }
                Row toAdd = new Row(newVal, newColHeader);
                operatedCol.addRow(toAdd);
            }
            return operatedCol;
        } catch (Exception e) {
            throw new IllegalArgumentException("ERROR: .*");
        }
    }

    private String overlap(String col1, String col2) {
        if (getNameType(col1).equals("int")) {
            if (getNameType(col2).equals("int")) {
                return "int";
            }
            if (getNameType(col2).equals("float")) {
                return "float";
            }
            throw new IllegalArgumentException("ERROR: Incompatible types: int and string");
        }
        if (getNameType(col1).equals("float")) {
            if (getNameType(col2).equals("string")) {
                throw new IllegalArgumentException("ERROR: Incompatible types: float and string");
            }
            return "float";
        }
        if (getNameType(col2).equals("int")) {
            throw new IllegalArgumentException("ERROR: Incompatible types: int and string");
        }
        if (getNameType(col2).equals("float")) {
            throw new IllegalArgumentException("ERROR: Incompatible types: float and string");
        }
        return "string";
    }

    public Table filter(String[] conditions) {
        Table currentCopy = new Table(getHeaders(), getColumnPrint());
        for (Row r : rows) {
            if (r.filtered(conditions)) {
                currentCopy.addRow(r);
            }
        }
        return currentCopy;
    }
}

