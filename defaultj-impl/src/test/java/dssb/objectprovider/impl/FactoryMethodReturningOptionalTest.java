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

import nawaman.nullablej.nullable.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class FactoryMethodReturningOptionalTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
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
