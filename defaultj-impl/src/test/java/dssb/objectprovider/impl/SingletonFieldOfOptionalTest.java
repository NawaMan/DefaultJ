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
import static org.junit.Assert.assertNull;

import nawaman.nullablej.nullable.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class SingletonFieldOfOptionalTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
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
