package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringJoiner;

public class Row<T> implements Iterable<TableItem> {
    // useful parsing variables
    private static final String SPACE = "\\s* \\s*",
    COMMA = "\\s*,\\s*";

    // Map for mapping column names to an array of items under that column
    private HashMap<String, Integer> headers;
    private HashMap<String, String> nameType;
    private ArrayList<TableItem> contents;
    private Integer size;


    /** constructor that takes in a String array of column names */
    public Row(TableItem[] row, String[] headers) {
        int count = 0;
        this.headers = new HashMap<>();
        this.nameType = new HashMap<>();
        for (String s : headers) {
            String[] nameType = s.split(SPACE);
            this.headers.put(s, count);
            this.nameType.put(nameType[0], nameType[1]);
            count += 1;
        }
        this.contents = new ArrayList<>();
        this.size = row.length;
        for (TableItem item : row) {
            contents.add(item);
        }
    }

    public int size() {
        return size;
    }

    /** Get method */
    public TableItem get(int index) {
        return contents.get(index);
    }

    /** add element to column */
    public void add(TableItem val) {
        contents.add(val);
        size += 1;
    }


    /** Get using header name */
    public TableItem get(String s) {
        return contents.get(headers.get(s));
    }

    public TableItem getWithoutType(String s) {
        return contents.get(headers.get(s + " " + nameType.get(s)));
    }

    public boolean contains(String s) {
        return headers.containsKey(s);
    }

    public String printRow() {
        String temp = "";
        for (int i = 0; i < contents.size() - 1; i += 1) {
            temp += contents.get(i).toString() + ",";
        }
        temp += contents.get(contents.size() - 1).toString();
        return temp + "\n";
    }

    private class rowIterator implements Iterator<TableItem> {
        private int indexer;

        public rowIterator() {
            indexer = 0;
        }

        public boolean hasNext() {
            return (indexer < size);
        }

        public TableItem next() {
            TableItem temp = contents.get(indexer);
            indexer += 1;
            return temp;
        }
    }

    @Override
    public Iterator<TableItem> iterator() {
        return new rowIterator();
    }

    public static boolean canJoin(Row a, Row b, String[] headers) {
        for (String s : headers) {
            if (a.contains(s) && b.contains(s)) {
                if (!a.get(s).equals(b.get(s))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Row join(Row a, Row b, String[] headers) {
        TableItem[] newValues = new TableItem[headers.length];
        int indexer = 0;
        for (String s : headers) {
            if (a.contains(s) && b.contains(s)) {
                if (a.get(s).equals(b.get(s)))
                    newValues[indexer] = a.get(s);
            } else if (a.contains(s)) {
                newValues[indexer] = a.get(s);
            } else {
                newValues[indexer] = b.get(s);
            }
            indexer += 1;
        }
        return new Row(newValues, headers);
    }

    private static String[] condSplitter(String cond) {
        String[] newConds = cond.split(SPACE);
        if (newConds.length > 3) {
            StringJoiner joiner = new StringJoiner("");
            for (int i = 2; i < newConds.length; i += 1) {
                joiner.add(newConds[i]);
            }
            newConds = new String[]{newConds[0], newConds[1], joiner.toString()};
        }
        return newConds;
    }

    public boolean filtered(String[] conditions) {
        for (String cond : conditions) {
            String[] conds = Row.condSplitter(cond);
            if (nameType.containsKey(conds[0]) && nameType.containsKey(conds[2])) {
                if (getWithoutType(conds[0]).get().equals("NOVALUE") ||
                        getWithoutType(conds[2]).get().equals("NOVALUE")) {
                    return false;
                }
                if (conds[1].equals(">=")) {
                    if (getWithoutType(conds[0]).compareTo(getWithoutType(conds[2])) < 0) {
                        return false;
                    }
                }
                if (conds[1].equals(">")) {
                    if (getWithoutType(conds[0]).compareTo(getWithoutType(conds[2])) <= 0) {
                        return false;
                    }
                }
                if (conds[1].equals("<=")) {
                    if (getWithoutType(conds[0]).compareTo(getWithoutType(conds[2])) > 0) {
                        return false;
                    }
                }
                if (conds[1].equals("<")) {
                    if (getWithoutType(conds[0]).compareTo(getWithoutType(conds[2])) >= 0) {
                        return false;
                    }
                }
                if (conds[1].equals("==")) {
                    if (getWithoutType(conds[0]).compareTo(getWithoutType(conds[2])) != 0) {
                        return false;
                    }
                }
                if (conds[1].equals("!=")) {
                    if (getWithoutType(conds[0]).compareTo(getWithoutType(conds[2])) == 0) {
                        return false;
                    }
                }
            } else if (nameType.containsKey(conds[0])) {
                if (getWithoutType(conds[0]).get().equals("NOVALUE")) {
                    return false;
                }
                TableItem primitive;
                if (conds[2].matches("[0-9]+")) {
                    Integer intCompare = Integer.parseInt(conds[2]);
                    primitive = new IntItem(intCompare);
                } else if (conds[2].matches("([0-9]*)\\.([0-9]*)")) {
                    Float floatCompare = Float.parseFloat(conds[2]);
                    primitive = new FloatItem(floatCompare);
                } else if (Character.toString(conds[2].charAt(0)).equals("'") &&
                        Character.toString(conds[2].charAt(conds[2].length() - 1)).equals("'")) {
                    String stringCompare = conds[2];
                    primitive = new StringItem(stringCompare);
                } else if (conds[2].equals("NOVALUE")) {
                    primitive = new NOVALUEItem();
                } else {
                    primitive = new NaNItem();
                }
                if (conds[1].equals(">=")) {
                    if (getWithoutType(conds[0]).compareTo(primitive) < 0) {
                        return false;
                    }
                }
                if (conds[1].equals(">")) {
                    if (getWithoutType(conds[0]).compareTo(primitive) <= 0) {
                        return false;
                    }
                }
                if (conds[1].equals("<=")) {
                    if (getWithoutType(conds[0]).compareTo(primitive) > 0) {
                        return false;
                    }
                }
                if (conds[1].equals("<")) {
                    if (getWithoutType(conds[0]).compareTo(primitive) >= 0) {
                        return false;
                    }
                }
                if (conds[1].equals("==")) {
                    if (getWithoutType(conds[0]).compareTo(primitive) != 0) {
                        return false;
                    }
                }
                if (conds[1].equals("!=")) {
                    if (getWithoutType(conds[0]).compareTo(primitive) == 0) {
                        return false;
                    }
                }
            } else {
                throw new IllegalArgumentException("ERROR: YOUR WHERE STATEMENT IS FUCKED UP SON");
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Row<?> row = (Row<?>) o;

        if (headers != null ? !headers.equals(row.headers) : row.headers != null) return false;
        if (contents != null ? !contents.equals(row.contents) : row.contents != null) return false;
        return size != null ? size.equals(row.size) : row.size == null;
    }

    @Override
    public int hashCode() {
        int result = headers != null ? headers.hashCode() : 0;
        result = 31 * result + (contents != null ? contents.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }
}
