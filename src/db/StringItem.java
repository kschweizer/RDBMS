package db;

import java.util.Comparator;

public class StringItem implements TableItem {
    String contents;
    public StringItem(String n) {
        String first = Character.toString(n.charAt(0));
        String last = Character.toString(n.charAt(n.length() - 1));
        if (!(first.equals("'") && last.equals("'"))) {
            throw new IllegalArgumentException("ERROR: Malformed data entry: strings must be passed with single quotes: '");
        } else {
            contents = n;
        }
    }

    @Override
    public String get() {
        return contents;
    }

    @Override
    public String toString() {
        return contents;
    }

    @Override
    public String typeToString() {
        return "String";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringItem that = (StringItem) o;

        return contents != null ? contents.equals(that.contents) : that.contents == null;
    }

    @Override
    public int hashCode() {
        return contents != null ? contents.hashCode() : 0;
    }

    @Override
    public TableItem add(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return this;
        }
        if (t.get() instanceof Integer) {
            throw new IllegalArgumentException("ERROR: Incompatible types: string and int");
        }
        if (t.get() instanceof Float) {
            throw new IllegalArgumentException("ERROR: Incompatible types: string and float");
        }
        String tString = (String) t.get();
        return new StringItem(contents.substring(0, contents.length()-1) + tString.substring(1));
    }

    @Override
    public TableItem sub(TableItem t) {
        throw new IllegalArgumentException("ERROR: Strings do not support subtraction");
    }

    @Override
    public TableItem mul(TableItem t) {
        throw new IllegalArgumentException("ERROR: Strings do not support multiplication");
    }

    @Override
    public TableItem div(TableItem t) {
        throw new IllegalArgumentException("ERROR: Strings do not support division");
    }

    @Override
    public int compareTo(Object o) {
        TableItem t = (TableItem) o;
        if (t.get().equals("NaN")) {
            return -1;
        }
        if (t.typeToString().equals("Integer")) {
            throw new IllegalArgumentException("ERROR: Incompatible types: string and int");
        }
        if (t.typeToString().equals("Float")) {
            throw new IllegalArgumentException("ERROR: Incompatible types: string and float");
        } else {
            String tString = (String) t.get();
            return contents.compareTo(tString);
        }
    }
}
