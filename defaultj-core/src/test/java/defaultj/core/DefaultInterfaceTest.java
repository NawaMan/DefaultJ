//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.
package defaultj.core;

import org.junit.Test;

import defaultj.annotations.DefaultInterface;
import defaultj.core.DefaultProvider;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("javadoc")
public class DefaultInterfaceTest {
    
    private DefaultProvider provider = new DefaultProvider();
    
    @DefaultInterface
    public static interface IGreet {
        public default String greet(String name) {
            return "Hello: " + name;
        }
    }
    
    @Test
    public void testSuccess_simple() {
        assertEquals("Hello: there",  provider.get(IGreet.class).greet("there"));
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
    
    @Test
    public void testSuccess_implementInChild() {
        assertEquals("Hello: world", provider.get(IGreet2Child.class).greet("world"));
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
    
    @Test
    public void testSuccess_implementInSuper() {
        assertEquals("Hello: world", provider.get(IGreet3Child.class).greet("world"));
    }
    
    // This class fails to compile on CIServer.
//    
//    public static interface IGreet4Super {
//        public String greet(String name);
//    }
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
//        } catch (ObjectCreationException e) {
//            val methods = ((NonDefaultInterfaceException)e.getCause()).getMethod();
//            assertEquals(
//                      "{"
//                    +   "greet([class java.lang.String])"
//                    +       ": class java.lang.String"
//                    +       "=nawaman.defaultj.impl.DefaultInterfaceTest.IGreet4Super"
//                    + "}",
//                    methods.toString());
//        }
//    }
    
}
