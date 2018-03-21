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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

@SuppressWarnings("javadoc")
@ExtensionMethod({ NullableJ.class })
public class DefaultImplementationTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultImplementation {
        
        public String value();
        
    }
    
    
    @DefaultImplementation("dssb.objectprovider.impl.TheClass2")
    public static interface TheInterface2 {
        
        public String getText();
        
    }
    
    public static class TheInterface2User {
        
        private TheInterface2 i2;
        
        public TheInterface2User(TheInterface2 i2) {
            this.i2 = i2;
        }
        
        public String getText() {
            return this.i2._whenNotNull().map(TheInterface2::getText).orElse("I am TheInterface2User.");
        }
        
    }
    
    @Test
    public void testThat_whenAnnotatedWithDefaultImplementation_findTheClassAndUseItsDefaultAsThis() {
        assertTrue(provider.get(TheInterface2.class) instanceof TheClass2);
        assertEquals(TheClass2.TEXT, provider.get(TheInterface2User.class).getText());
    }

    @DefaultImplementation(value="directget.get.TheClassThatDoesNotExist")
    public static interface TheInterface3 {
        
        public String getText();
        
    }
    
    public static class TheInterface3User {
        
        public static final String TEXT = "I am TheInterface3User.";
        
        private TheInterface3 i3;
        
        public TheInterface3User(TheInterface3 i3) {
            this.i3 = i3;
        }
        
        public String getText() {
            return this.i3._whenNotNull().map(TheInterface3::getText).orElse(TEXT);
        }
        
    }
    
    @Test
    public void testThat_whenAnnotatedWithDefaultImplementation_findTheClassAndUseItsDefaultAsThis_nullWhenNotExist() {
        assertEquals(TheInterface3User.TEXT, provider.get(TheInterface3User.class).getText());
    }
    
}
