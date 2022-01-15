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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;

import defaultj.core.bindings.FactoryBinding;

@SuppressWarnings("javadoc")
public class ConstructorWithSupplierTest {
    
    public static class Company {
        private Supplier<Integer> revenueSupplier;
        public Company(Supplier<Integer> revenueSupplier) {
            this.revenueSupplier = revenueSupplier;
        }
        public int revenue() {
            return revenueSupplier.get();
        }
    }
    
    @Test
    public void testThat_withSupplierAsParameter_aSupplierToGetIsGiven() {
        var counter        = new AtomicInteger(1000);
        var factoryBinding = new FactoryBinding<Integer>(defaultProvider->counter.getAndIncrement());
        
        var bindings = new Bindings.Builder().bind(Integer.class, factoryBinding).build();
        var provider = new DefaultProvider().wihtBindings(bindings);
        
        var company = provider.get(Company.class);
        
        assertEquals(1000, company.revenue());
        assertEquals(1001, company.revenue());
        assertEquals(1002, company.revenue());
    }
    
}
