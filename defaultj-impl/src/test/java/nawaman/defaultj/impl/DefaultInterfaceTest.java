package nawaman.defaultj.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import lombok.val;
import nawaman.defaultj.annotations.DefaultInterface;
import nawaman.defaultj.impl.ObjectProvider;
import nawaman.defaultj.impl.exception.NonDefaultInterfaceException;
import nawaman.defaultj.impl.exception.ObjectCreationException;

@SuppressWarnings("javadoc")
public class DefaultInterfaceTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    @DefaultInterface
    public static interface IGreet {
        public default String greet(String name) {
            return "Hello: " + name;
        }
    }
    
    public static interface IGreet2Super {
        public String greet(String name);
    }
    
    @DefaultInterface
    public static interface IGreet2Child extends IGreet2Super {
        public default String greet(String name) {
            return "Hello: " + name;
        }
    }
    
    public static interface IGreet3Super {
        public default String greet(String name) {
            return "Hello: " + name;
        }
    }

    @DefaultInterface
    public static interface IGreet3Child extends IGreet3Super {
        public String greet(String name);
    }
    
    public static interface IGreet4Super {
        public String greet(String name);
    }
    
    @DefaultInterface
    public static interface IGreet4Child extends IGreet4Super {
        
    }
    
    @Test
    public void testSuccess_simple() {
        assertEquals("Hello: there",  provider.get(IGreet.class).greet("there"));
        provider.get(IGreet2Child.class);
    }
    
    @Test
    public void testSuccess_implementInChild() {
        provider.get(IGreet2Child.class);
    }
    
    @Test
    public void testSuccess_implementInSuper() {
        provider.get(IGreet3Child.class);
    }
    
    @Test
    public void testFail_hasNonDefaultMethod() {
        try {
            provider.get(IGreet4Child.class);
        } catch (ObjectCreationException e) {
            val methods = ((NonDefaultInterfaceException)e.getCause()).getMethod();
            assertEquals(
                      "{"
                    +   "greet([class java.lang.String])"
                    +       ": class java.lang.String"
                    +       "=nawaman.defaultj.impl.DefaultInterfaceTest.IGreet4Super"
                    + "}",
                    methods.toString());
        }
    }
    
}
