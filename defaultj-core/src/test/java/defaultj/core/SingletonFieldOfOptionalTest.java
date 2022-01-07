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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

import org.junit.Test;

import nullablej.nullable.Nullable;

@SuppressWarnings("javadoc")
public class SingletonFieldOfOptionalTest {
    
    private DefaultProvider provider = new DefaultProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static class OptionalSingleton {
        @Default
        public static final Optional<OptionalSingleton> instance = Optional.of(new OptionalSingleton());
        
        private OptionalSingleton() {}
    }
    
    @Test
    public void testThat_optionalSingletonClassWithDefaultAnnotationHasTheInstanceAsTheValue() {
        OptionalSingleton value = provider.get(OptionalSingleton.class);
        assertEquals(OptionalSingleton.instance.get(), value);
    }
    
    
    public static class EmptyOptionalSingleton {
        @Default
        public static final Optional<EmptyOptionalSingleton> instance = Optional.empty();
        
        private EmptyOptionalSingleton() {}
    }
    
    @Test
    public void testThat_optionalSingletonClassWithDefaultAnnotationHasTheInstanceAsTheValue_empty() {
        EmptyOptionalSingleton value = provider.get(EmptyOptionalSingleton.class);
        assertEquals(EmptyOptionalSingleton.instance.orElse(null), value);
        assertNull(value);
    }
    
    
    public static class NullableSingleton {
        @Default
        public static final Nullable<NullableSingleton> instance = Nullable.of(new NullableSingleton());
        
        private NullableSingleton() {}
    }
    
    @Test
    public void testThat_nullableSingletonClassWithDefaultAnnotationHasTheInstanceAsTheValue() {
        NullableSingleton value = provider.get(NullableSingleton.class);
        assertEquals(NullableSingleton.instance.get(), value);
    }
    
    
    public static class EmptyNullableSingleton {
        @Default
        public static final Nullable<EmptyNullableSingleton> instance = Nullable.empty();
        
        private EmptyNullableSingleton() {}
    }
    
    @Test
    public void testThat_nullableSingletonClassWithDefaultAnnotationHasTheInstanceAsTheValue_empty() {
        EmptyNullableSingleton value = provider.get(EmptyNullableSingleton.class);
        assertEquals(EmptyNullableSingleton.instance.orElse(null), value);
        assertNull(value);
    }
    
}
