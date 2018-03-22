package nawaman.defaultj.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import nawaman.defaultj.core.DefaultImplementationTest.DefaultImplementation;
import nawaman.defaultj.core.exception.ImplementedClassNotCompatibleExistException;

@SuppressWarnings("javadoc")
public class ImplementedByTest {

    private DefaultProvider provider = new DefaultProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ImplementedBy {
        
        public Class<?> value();
        
    }
    
    @ImplementedBy(TheClass.class)
    @DefaultImplementation("non.exist.class")
    public static interface TheInterface {
        
        public String getText();
        
    }
    public static class TheClass implements TheInterface {
        
        public static final String TEXT = "I the class.";
        
        @Override
        public String getText() {
            return TEXT;
        }
        
    }
    
    @Test
    public void testImplementationNameNotExistSoClassIsUsed() {
        assertEquals(TheClass.class, provider.get(TheInterface.class).getClass());
    }
    
    @ImplementedBy(TheClass2.class)
    @DefaultImplementation("nawaman.defaultj.core.TheClass3")
    public static interface TheInterface2 {
        
        public String getText();
        
    }
    public static class TheClass2 implements TheInterface2 {
        
        public static final String TEXT = "I the class.";
        
        @Override
        public String getText() {
            return TEXT;
        }
        
    }
    
    @Test
    public void testImplementationNameExistSoItIsUsed() {
        assertEquals(TheClass3.class, provider.get(TheInterface2.class).getClass());
    }
    
    @ImplementedBy(Object.class)
    public static interface TheInterface3 {
        
        public String getText();
        
    }
    
    @Test(expected=ImplementedClassNotCompatibleExistException.class)
    public void testImplementationNotCompatible() {
        provider.get(TheInterface3.class);
    }
}
