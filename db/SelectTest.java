package db;

import org.junit.Test;

import java.util.Arrays;
import java.util.StringJoiner;

public class SelectTest {
    public static final String SPACE = "\\s* \\s*";
    @Test
    public void regexTester() {
        String s = "x*y as q";
        String s3 = "x * y as q";
        String[] s1 = new String[1];
        String[] s4;
        s4 = s3.split("[*]");
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = 0; i < s4.length; i++) {
            joiner.add(s4[i]);
        }
        String shit = joiner.toString();
        String[] whatweactuallywant = shit.split(SPACE);
    }
}
