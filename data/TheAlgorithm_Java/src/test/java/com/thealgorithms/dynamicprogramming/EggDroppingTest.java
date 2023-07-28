package data.TheAlgorithm_Java.src.test.java.com.thealgorithms.dynamicprogramming;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EggDroppingTest {

    @Test
    void hasMultipleEggSingleFloor(){
        assertEquals(1,EggDropping.minTrials(3,1));
    }

    @Test
    void hasSingleEggSingleFloor(){
        assertEquals(1,EggDropping.minTrials(1,1));
    }

    @Test
    void hasSingleEggMultipleFloor(){
        assertEquals(3,EggDropping.minTrials(1,3));
    }

    @Test
    void hasMultipleEggMultipleFloor(){
        assertEquals(7,EggDropping.minTrials(100,101));
    }
}
