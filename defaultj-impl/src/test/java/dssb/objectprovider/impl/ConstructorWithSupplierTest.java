//  ========================================================================
//  Copyright (c) 2017 Direct Solution Software Builders (DSSB).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package dssb.objectprovider.impl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import dssb.objectprovider.impl.bindings.FactoryBinding;
import lombok.val;

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
        val counter        = new AtomicInteger(1000);
        val factoryBinding = new FactoryBinding<Integer>(objectProvider->counter.getAndIncrement());
        
        val bindings = new Bindings.Builder().bind(Integer.class, factoryBinding).build();
        val provider = new ObjectProvider().wihtBindings(bindings);
        
        val company = provider.get(Company.class);
        
        assertEquals(1000, company.revenue());
        assertEquals(1001, company.revenue());
        assertEquals(1002, company.revenue());
    }
    
}
