package db;

/**
 * Created by J-Murda on 3/6/17.
 */
public class NOVALUEItem implements TableItem{
    String contents = "NOVALUE";

    @Override
    public String get() { return contents; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NOVALUEItem)) return false;

        NOVALUEItem that = (NOVALUEItem) o;

        return contents != null ? contents.equals(that.contents) : that.contents == null;
    }

    @Override
    public int hashCode() {
        return contents != null ? contents.hashCode() : 0;
    }

    @Override
    public String toString() {
        return contents;
    }

    @Override
    public String typeToString() {
        return "";
    }

    @Override
    public TableItem add(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return t;
        }
        if (t.get() instanceof Integer) {
            return new IntItem((Integer) t.get());
        }
        if (t.get() instanceof Float) {
            return new FloatItem((Float) t.get());
        }
        return new StringItem((String) t.get());
    }

    @Override
    public TableItem sub(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return t;
        }
        if (t.get() instanceof Integer) {
            return new IntItem(-((Integer) t.get()));
        }
        if (t.get() instanceof Float) {
            return new FloatItem(-((Float) t.get()));
        }
        throw new IllegalArgumentException("ERROR: Strings do not support subtraction");
    }

    @Override
    public TableItem mul(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return t;
        }
        if (t.get() instanceof Integer) {
            return new IntItem(0);
        }
        if (t.get() instanceof Float) {
            return new FloatItem((float) 0.0);
        }
        throw new IllegalArgumentException("ERROR: Strings do not support multiplication");
    }

    @Override
    public TableItem div(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return t;
        }
        if (t.get() instanceof Integer) {
            return new IntItem(0);
        }
        if (t.get() instanceof Float) {
            return new FloatItem((float) 0.0);
        }
        throw new IllegalArgumentException("ERROR: Strings do not support division");
    }

    @Override
    public int compareTo(Object o) {
        return -1;
    }
}
