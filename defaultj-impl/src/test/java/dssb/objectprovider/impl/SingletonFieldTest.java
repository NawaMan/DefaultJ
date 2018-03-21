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
public class SingletonFieldTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static class BasicSingleton {
        @Default
        public static final BasicSingleton instance = new BasicSingleton("instance");
        
        private String string;
        
        private BasicSingleton(String string) {
            this.string = string;
        }
        
        @Default
        public static BasicSingleton newInstance() {
            return new BasicSingleton("factory");
        }
    }
    
    @Test
    public void testThat_singletonClassWithDefaultAnnotationHasTheInstanceAsTheValue_withFieldMorePreferThanFactory() {
        assertEquals(BasicSingleton.instance, provider.get(BasicSingleton.class));
        assertEquals("instance", provider.get(BasicSingleton.class).string);
    }
    
}
