package db;

/**
 * Created by J-Murda on 3/6/17.
 */
public class NaNItem implements TableItem{
    String contents = "NaN";

    @Override
    public String get() { return contents; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NaNItem)) return false;

        NaNItem naNItem = (NaNItem) o;

        return contents != null ? contents.equals(naNItem.contents) : naNItem.contents == null;
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
        return new NaNItem();
    }

    @Override
    public TableItem sub(TableItem t) {
        return new NaNItem();
    }

    @Override
    public TableItem mul(TableItem t) {
        return new NaNItem();
    }

    @Override
    public TableItem div(TableItem t) {
        return new NaNItem();
    }

    @Override
    public int compareTo(Object o) {
        TableItem t = (TableItem) o;
        if (t.get().equals("NaN")) {
            return 0;
        }
        return 1;
    }

}
