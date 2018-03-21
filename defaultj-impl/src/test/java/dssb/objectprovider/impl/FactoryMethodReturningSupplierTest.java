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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import nawaman.failable.Failable;

@SuppressWarnings("javadoc")
public class FactoryMethodReturningSupplierTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static class SupplierFactoryMethodFactoryMethod {
        
        private static int counter = 0;
        private String string;
        
        private SupplierFactoryMethodFactoryMethod(String string) {
            this.string = string;
        }
        
        @Default
        public static Supplier<SupplierFactoryMethodFactoryMethod> newInstance() {
            return ()->{
                counter++;
                return new SupplierFactoryMethodFactoryMethod("factory");
            };
        }
    }
    
    @Test
    public void testThat_classWithFactoryMethodDefaultAnnotationHasTheResultAsTheValue_supplier() {
        int prevCounter = SupplierFactoryMethodFactoryMethod.counter;
        
        assertEquals("factory", provider.get(SupplierFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 1, SupplierFactoryMethodFactoryMethod.counter);
        
        assertEquals("factory", provider.get(SupplierFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 2, SupplierFactoryMethodFactoryMethod.counter);
    }
    
    public static class FailableSupplierFactoryMethodFactoryMethod {
        
        private static int counter = 0;
        private String string;
        
        private FailableSupplierFactoryMethodFactoryMethod(String string) {
            this.string = string;
        }
        
        @Default
        public static Failable.Supplier<SupplierFactoryMethodFactoryMethod, RuntimeException> newInstance() {
            return ()->{
                counter++;
                return new SupplierFactoryMethodFactoryMethod("factory");
            };
        }
        
        public static int getCounter() {
            return counter;
        }
        
        public String getString() {
            return string;
        }
        
        
    }
    
    @Test
    public void testThat_classWithFactoryMethodDefaultAnnotationHasTheResultAsTheValue_supplier_failable() {
        int prevCounter = SupplierFactoryMethodFactoryMethod.counter;
        
        assertEquals("factory", provider.get(SupplierFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 1, SupplierFactoryMethodFactoryMethod.counter);
        
        assertEquals("factory", provider.get(SupplierFactoryMethodFactoryMethod.class).string);
        assertEquals(prevCounter + 2, SupplierFactoryMethodFactoryMethod.counter);
    }
    
}
