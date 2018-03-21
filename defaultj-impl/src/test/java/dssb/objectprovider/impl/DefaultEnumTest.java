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

import org.junit.Test;

@SuppressWarnings("javadoc")
public class DefaultEnumTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static enum MyEnum1 { Value1, Value2; }
    
    @Test
    public void testThat_enumValue_isTheFirstValue() {
        assertEquals(MyEnum1.Value1, provider.get(MyEnum1.class));
    }
    
    public static enum MyEnum2 { Value1, @Default Value2; }
    
    @Test
    public void testThat_enumValueWithDefaultAnnotation() {
        assertEquals(MyEnum2.Value2, provider.get(MyEnum2.class));
    }
    
    public static enum EmptyEnum { }
    
    @Test
    public void testThat_emptyEnumValue_isnull() {
        assertEquals(null, provider.get(EmptyEnum.class));
    }
    
}
