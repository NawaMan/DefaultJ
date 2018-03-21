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

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Supplier;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class SingletonFieldSupplierTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static class SupplierSingleton {
        
        private static int counter = 0;
        
        private static final SupplierSingleton secretInstance = new SupplierSingleton();
        
        @Default
        public static final Supplier<SupplierSingleton> instance = ()->{
            counter++;
            return secretInstance;
        };
        
        private SupplierSingleton() {}
    }
    
    @Test
    public void testThat_supplierSingletonClassWithDefaultAnnotationHasResultOfThatInstanceAsValue() {
        int prevCounter = SupplierSingleton.counter;
        
        assertEquals(SupplierSingleton.secretInstance, provider.get(SupplierSingleton.class));
        assertEquals(prevCounter + 1, SupplierSingleton.counter);
        
        assertEquals(SupplierSingleton.secretInstance, provider.get(SupplierSingleton.class));
        assertEquals(prevCounter + 2, SupplierSingleton.counter);
    }
    
}
