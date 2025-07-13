//package defaultj.core;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Test;
//
//import defaultj.annotations.DefaultInterface;
//import defaultj.core.DefaultInterfaceTest.IGreet4Super;
//import defaultj.core.exception.NonDefaultInterfaceException;
//
// // Comment out as it will fail to compile.
//public class DefaultInterfaceFailureTest {
//    
//    private DefaultProvider provider = new DefaultProvider();
//    
//    @DefaultInterface
//    public static interface IGreet4Child extends IGreet4Super {
//        
//    }
//    
//    @Test
//    public void testFail_hasNonDefaultMethod() {
//        try {
//            provider.get(IGreet4Child.class);
//        } catch (NonDefaultInterfaceException e) {
//            var methods = e.getMethod();
//            assertEquals(
//                      "{"
//                    +   "greet([class java.lang.String])"
//                    +       ": class java.lang.String"
//                    +       "=defaultj.core.DefaultInterfaceTest.IGreet4Super"
//                    + "}",
//                    methods.toString());
//        }
//    }
//    
//}
