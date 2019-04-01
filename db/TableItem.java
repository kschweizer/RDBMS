package db;

public interface TableItem {
    public Object get();

    public boolean equals(Object t);

    public String toString();

    public String typeToString();

    public TableItem add(TableItem t);

    public TableItem sub(TableItem t);

    public TableItem mul(TableItem t);

    public TableItem div(TableItem t);

    public int compareTo(Object o);
}

