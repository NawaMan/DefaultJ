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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Test;

import defaultj.annotations.Nullable;
import defaultj.core.exception.CyclicDependencyDetectedException;
import defaultj.core.exception.DefaultCreationException;
import lombok.val;

public class CyclicDependencyDetectionTest {
    
    private DefaultProvider provider = new DefaultProvider();
    
    public static class Cyclic1 {
        
        public Cyclic1(Cyclic1 another) {
            System.err.println(another);
        }
    }
    
    @Test(expected=CyclicDependencyDetectedException.class)
    public void testThat_whenDefaultConstructorAskForItself_expectCyclicDependencyDetectedException() {
        provider.get(Cyclic1.class);
        fail("Expect an exception");
    }
    
    public static class Cyclic2 {
        
        public Cyclic2(Optional<Cyclic2> another) {
            System.err.println(another);
        }
    }
    
    @Test
    public void testThat_whenDefaultConstructorAskForItself_expectCyclicDependencyDetectedException_evenWithOptional() {
        try {
            provider.get(Cyclic2.class);
        } catch (DefaultCreationException e) {
            assertTrue(e.getCause() instanceof CyclicDependencyDetectedException);
        }
    }
    
    public static class Cyclic3 {
        
        public Cyclic3(@Nullable Cyclic3 another) {
            System.err.println(another);
        }
    }
    
    @Test
    public void testThat_whenDefaultConstructorAskForItself_expectCyclicDependencyDetectedException_evenWithNullable() {
        try {
            provider.get(Cyclic3.class);
        } catch (DefaultCreationException e) {
            assertTrue(e.getCause() instanceof CyclicDependencyDetectedException);
        }
    }
    
    public static class Cyclic4 {
        
        public Cyclic4(Supplier<Cyclic4> another) {
            System.err.println(another);
        }
    }
    
    @Test
    public void testThat_whenDefaultConstructorAskForSupplierItself_itGetOne() {
        val cyclic4 = provider.get(Cyclic4.class);
        assertNotNull(cyclic4);
    }
    
}
