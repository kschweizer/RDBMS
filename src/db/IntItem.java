package db;
import java.util.Comparator;

public class IntItem implements TableItem {
    Integer contents;
    public IntItem(int n) {
        this.contents = n;
    }

    @Override
    public Integer get() {
        return contents;
    }

    @Override
    public String toString() {
        return contents.toString();
    }

    @Override
    public String typeToString() {
        return "Integer";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntItem intItem = (IntItem) o;

        return contents != null ? contents.equals(intItem.contents) : intItem.contents == null;
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
            return new IntItem(contents + (Integer) t.get());
        }
        if (t.get() instanceof Float) {
            return new FloatItem(contents + (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: int and string");
    }

    @Override
    public TableItem sub(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return this;
        }
        if (t.get() instanceof Integer) {
            return new IntItem(contents - (Integer) t.get());
        }
        if (t.get() instanceof Float) {
            return new FloatItem(contents - (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: int and string");
    }

    @Override
    public TableItem mul(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return new IntItem(0);
        }
        if (t.get() instanceof Integer) {
            return new IntItem(contents * (Integer) t.get());
        }
        if (t.get() instanceof Float) {
            return new FloatItem(contents * (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: int and string");
    }

    @Override
    public TableItem div(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return new NaNItem();
        }
        if (t.get() instanceof Integer) {
            try {
                return new IntItem(contents / (Integer) t.get());
            } catch (Exception e) {
                return new NaNItem();
            }
        }
        if (t.get() instanceof Float) {
            if ((contents / (Float) t.get()) == Double.POSITIVE_INFINITY ||
                    (contents / (Float) t.get()) == Double.NEGATIVE_INFINITY) {
                return new NaNItem();
            }
            return new FloatItem(contents / (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: int and string");
    }
    @Override
    public int compareTo(Object o) {
        TableItem t = (TableItem) o;
        if (t.get().equals("NaN")) {
            return -1;
        }
        if (t.typeToString().equals("Integer")) {
            if (get() > (Integer) t.get()) {
                return 1;
            }
            if (get() < (Integer) t.get()) {
                return -1;
            }
            return 0;
        }
        if (t.typeToString().equals("Float")) {
            if (get() > (Float) t.get()) {
                return 1;
            }
            if (get() < (Float) t.get()) {
                return -1;
            }
            return 0;
        } else {
            throw new IllegalArgumentException("ERROR: Incompatible types: int and string");
        }
    }
}
