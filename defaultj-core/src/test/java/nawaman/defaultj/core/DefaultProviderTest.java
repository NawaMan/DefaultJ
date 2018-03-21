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
package nawaman.defaultj.core;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.DefaultProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class DefaultProviderTest {
    
    private IProvideDefault provider = IProvideDefault.defaultProvider().get();
    
    @Test
    public void testDefaultProvider() {
        assertTrue(DefaultProvider.class.isInstance(IProvideDefault.defaultProvider().get()));
    }
    
    @Test
    public void testDefaultPrimitives() {
        assertEquals(false, provider.get(boolean.class));
        assertEquals(false, provider.get(Boolean.class));
        
        assertEquals((byte)0, provider.get(byte.class).byteValue());
        assertEquals((byte)0, provider.get(Byte.class).byteValue());
        
        assertEquals((short)0, provider.get(short.class).shortValue());
        assertEquals((short)0, provider.get(Short.class).shortValue());
        
        assertEquals(0, provider.get(int.class).intValue());
        assertEquals(0, provider.get(Integer.class).intValue());
        
        assertEquals(0L, provider.get(long.class).longValue());
        assertEquals(0L, provider.get(Long.class).longValue());
        
        assertEquals(' ', provider.get(char.class).charValue());
        assertEquals(' ', provider.get(Character.class).charValue());
        
        assertEquals("", provider.get(String.class));
        
        assertNull(provider.get(Supplier.class).get());
        
        assertTrue(provider.get(List.class).isEmpty());
        assertTrue(provider.get(Set.class).isEmpty());
        assertTrue(provider.get(Map.class).isEmpty());

        assertTrue(provider.get(String[].class).length == 0);
    }
    
}
