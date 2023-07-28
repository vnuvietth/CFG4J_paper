package data.TheAlgorithm_Java.src.test.java.com.thealgorithms.conversions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HexaDecimalToBinaryTest {

    @Test
    public void testHexaDecimalToBinary(){
        HexaDecimalToBinary hexaDecimalToBinary = new HexaDecimalToBinary();
        assertEquals("1111111111111111111111111111111", hexaDecimalToBinary.convert("7fffffff"));
        assertEquals("101010111100110111101111", hexaDecimalToBinary.convert("abcdef"));
    }
}
