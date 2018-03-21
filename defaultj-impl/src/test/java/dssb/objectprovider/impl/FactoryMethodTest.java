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
public class FactoryMethodTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static class BasicFactoryMethod {
        
        private static int counter = 0;
        
        public static final BasicFactoryMethod instance = new BasicFactoryMethod("instance");
        
        private String string;
        
        private BasicFactoryMethod(String string) {
            this.string = string;
        }
        
        @Default
        public static BasicFactoryMethod newInstance() {
            counter++;
            return new BasicFactoryMethod("factory");
        }
    }
    
    @Test
    public void testThat_classWithFactoryMethodDefaultAnnotationHasTheInstanceAsTheValue() {
        int prevCounter = BasicFactoryMethod.counter;
        
        assertEquals("factory", provider.get(BasicFactoryMethod.class).string);
        assertEquals(prevCounter + 1, BasicFactoryMethod.counter);
        
        assertEquals("factory", provider.get(BasicFactoryMethod.class).string);
        assertEquals(prevCounter + 2, BasicFactoryMethod.counter);
    }
    
}
