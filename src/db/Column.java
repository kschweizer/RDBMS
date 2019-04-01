package db;
import java.util.ArrayList;
import java.util.Iterator;

public class Column<T> implements Iterable<T> {

    /** Map for mapping column names to an array of items under that column */
     private String header;
     private ArrayList<T> contents;
     private Integer size;


     /** constructor that takes in a String as a header name */
    public Column(String header) {
        this.header = header;
        this.contents = new ArrayList<T>();
        this.size = 0;
    }

    /** Get method */
    public T get(int index) {
        return contents.get(index);
    }

    /** add element to column */
    public void add(T val) {
        contents.add(val);
        size += 1;
    }

    public String getHeader() {
        return header;
    }

    private class columnIterator implements Iterator<T> {
        private int indexer;

        public columnIterator() {
            indexer = 0;
        }

        public boolean hasNext() {
            return (indexer < size);
        }

        public T next() {
            T temp = contents.get(indexer);
            indexer += 1;
            return temp;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new columnIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column<?> column = (Column<?>) o;

        if (header != null ? !header.equals(column.header) : column.header != null) return false;
        if (contents != null ? !contents.equals(column.contents) : column.contents != null) return false;
        return size != null ? size.equals(column.size) : column.size == null;
    }

    @Override
    public int hashCode() {
        int result = header != null ? header.hashCode() : 0;
        result = 31 * result + (contents != null ? contents.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }
}
