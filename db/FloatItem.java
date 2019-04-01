package db;
import java.util.Comparator;

public class FloatItem implements TableItem {
    Float contents;
    public FloatItem(float n) {
        this.contents = n;
    }

    @Override
    public Float get() {
        return contents;
    }

    @Override
    public String toString() {
        String temp = String.format("%.3f", contents);
        return temp;
    }

    @Override
    public String typeToString() {
        return "Float";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatItem floatItem = (FloatItem) o;

        return contents != null ? contents.equals(floatItem.contents) : floatItem.contents == null;
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
            return new FloatItem(contents + (Integer) t.get());
        }
        if (t.get() instanceof Float) {
            return new FloatItem(contents + (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: float and string");
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
            return new FloatItem(contents - (Integer) t.get());
        }
        if (t.get() instanceof Float) {
            return new FloatItem(contents - (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: float and string");
    }

    @Override
    public TableItem mul(TableItem t) {
        if (t.get().equals("NaN")) {
            return t;
        }
        if (t.get().equals("NOVALUE")) {
            return new FloatItem((float) 0.0);
        }
        if (t.get() instanceof Integer) {
            return new FloatItem(contents * (Integer) t.get());
        }
        if (t.get() instanceof Float) {
            return new FloatItem(contents * (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: float and string");
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
            if ((contents / (Integer) t.get()) == Double.POSITIVE_INFINITY ||
                    (contents / (Integer) t.get()) == Double.NEGATIVE_INFINITY) {
                return new NaNItem();
            }
            return new FloatItem(contents / (Integer) t.get());
        }
        if (t.get() instanceof Float) {
            if ((contents / (Float) t.get()) == Double.POSITIVE_INFINITY) {
                return new NaNItem();
            }
            return new FloatItem(contents / (Float) t.get());
        }
        throw new IllegalArgumentException("ERROR: Incompatible types: float and string");
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
