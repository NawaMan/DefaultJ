//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
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

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

import org.junit.Test;

import nullablej.nullable.Nullable;

public class FactoryMethodReturningOptionalTest {
    
    private DefaultProvider provider = new DefaultProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static class OptionalFactoryMethodFactoryMethod {
        
        private static int counter = 0;
        private String string;
        
        private OptionalFactoryMethodFactoryMethod(String string) {
            this.string = string;
        }
        
        @Default
        public static Optional<OptionalFactoryMethodFactoryMethod> newInstance() {
            counter++;
            return Optional.of(new OptionalFactoryMethodFactoryMethod("factory"));
        }
    }
    
    @Test
    public void testThat_classWithFactoryMethodDefaultAnnotationHasTheResultAsTheValue_optional() {
        int prevCounter = OptionalFactoryMethodFactoryMethod.counter;
        
        assertEquals("factory", provider.get(OptionalFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 1, OptionalFactoryMethodFactoryMethod.counter);
        
        assertEquals("factory", provider.get(OptionalFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 2, OptionalFactoryMethodFactoryMethod.counter);
    }
    
    public static class NullableFactoryMethodFactoryMethod {
        
        private static int counter = 0;
        private String string;
        
        private NullableFactoryMethodFactoryMethod(String string) {
            this.string = string;
        }
        
        @Default
        public static Nullable<NullableFactoryMethodFactoryMethod> newInstance() {
            counter++;
            return Nullable.of(new NullableFactoryMethodFactoryMethod("factory"));
        }
    }
    
    @Test
    public void testThat_classWithFactoryMethodDefaultAnnotationHasTheResultAsTheValue_nullalbe() {
        int prevCounter = NullableFactoryMethodFactoryMethod.counter;
        
        assertEquals("factory", provider.get(NullableFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 1, NullableFactoryMethodFactoryMethod.counter);
        
        assertEquals("factory", provider.get(NullableFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 2, NullableFactoryMethodFactoryMethod.counter);
    }
    
}
