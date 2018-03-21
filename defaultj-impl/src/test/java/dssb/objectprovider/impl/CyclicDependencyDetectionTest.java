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

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import dssb.objectprovider.annotations.Nullable;
import dssb.objectprovider.impl.exception.CyclicDependencyDetectedException;
import dssb.objectprovider.impl.exception.ObjectCreationException;
import lombok.val;

@SuppressWarnings("javadoc")
public class CyclicDependencyDetectionTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    public static class Cyclic1 {
        
        public Cyclic1(Cyclic1 another) {
        }
    }
    
    @Test
    public void testThat_whenDefaultConstructorAskForItself_expectCyclicDependencyDetectedException() {
        try {
            provider.get(Cyclic1.class);
            fail("Expect an exception");
        } catch (ObjectCreationException e) {
            assertTrue(e.getCause() instanceof CyclicDependencyDetectedException);
        }
    }
    
    public static class Cyclic2 {
        
        public Cyclic2(Optional<Cyclic2> another) {
        }
    }
    
    @Test
    public void testThat_whenDefaultConstructorAskForItself_expectCyclicDependencyDetectedException_evenWithOptional() {
        try {
            provider.get(Cyclic2.class);
        } catch (ObjectCreationException e) {
            assertTrue(e.getCause() instanceof CyclicDependencyDetectedException);
        }
    }
    
    public static class Cyclic3 {
        
        public Cyclic3(@Nullable Cyclic3 another) {
        }
    }
    
    @Test
    public void testThat_whenDefaultConstructorAskForItself_expectCyclicDependencyDetectedException_evenWithNullable() {
        try {
            provider.get(Cyclic3.class);
        } catch (ObjectCreationException e) {
            assertTrue(e.getCause() instanceof CyclicDependencyDetectedException);
        }
    }
    
    public static class Cyclic4 {
        
        public Cyclic4(Supplier<Cyclic4> another) {
        }
    }
    
    @Test
    public void testThat_whenDefaultConstructorAskForSupplierItself_itGetOne() {
        val cyclic4 = provider.get(Cyclic4.class);
        assertNotNull(cyclic4);
    }
    
}
