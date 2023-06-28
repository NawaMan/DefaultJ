package defaultj.core.strategies;

import static defaultj.core.strategies.common.extractValue;
import static org.junit.Assert.*;

import org.junit.Test;

public class ImplementedBySupplierFinderTest {
    
    @Test
    public void testExtractWithoutValueWithoutClass() {
        assertEquals(
                "defaultj.core.ImplementedByTest$TheClass2",
                extractValue.apply("@defaultj.core.ImplementedByTest$ImplementedBy(defaultj.core.ImplementedByTest$TheClass2)"));
    }
    
    @Test
    public void testExtractWithValueWithoutClass() {
        assertEquals(
                "defaultj.core.ImplementedByTest$TheClass2",
                extractValue.apply("@defaultj.core.ImplementedByTest$ImplementedBy(value=defaultj.core.ImplementedByTest$TheClass2)"));
    }
    @Test
    public void testExtractWithoutValueWithClass() {
        assertEquals(
                "defaultj.core.ImplementedByTest$TheClass2",
                extractValue.apply("@defaultj.core.ImplementedByTest$ImplementedBy(defaultj.core.ImplementedByTest$TheClass2.class)"));
    }
    
    @Test
    public void testExtractWithValueWithClass() {
        assertEquals(
                "defaultj.core.ImplementedByTest$TheClass2",
                extractValue.apply("@defaultj.core.ImplementedByTest$ImplementedBy(value=defaultj.core.ImplementedByTest$TheClass2.class)"));
    }
    
}
