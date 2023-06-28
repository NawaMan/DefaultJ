//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.
package defaultj.core;

import static nullablej.nullable.Nullable.nullable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import defaultj.core.DefaultToNullTest.DefaultToNull;
import defaultj.core.exception.AbstractClassCreationException;

public class DefaultImplementationTest {
    
    private DefaultProvider provider = new DefaultProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultImplementation {
        
        public String value();
        
    }
    
    
    @DefaultImplementation("defaultj.core.TheClass2")
    @DefaultToNull
    public static interface TheInterface2 {
        
        public String getText();
        
    }
    
    public static class TheInterface2User {
        
        private TheInterface2 i2;
        
        public TheInterface2User(TheInterface2 i2) {
            this.i2 = i2;
        }
        
        public String getText() {
            return nullable(this.i2).map(TheInterface2::getText).orElse("I am TheInterface2User.");
        }
        
    }
    
    @Test
    public void testThat_whenAnnotatedWithDefaultImplementation_findTheClassAndUseItsDefaultAsThis() {
        System.out.println(provider.get(TheInterface2.class));
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
            return nullable(this.i3).map(TheInterface3::getText).orElse(TEXT);
        }
        
    }
    
    @Test(expected=AbstractClassCreationException.class)
    public void testThat_whenAnnotatedWithDefaultImplementation_findTheClassAndUseItsDefaultAsThis_throwException() {
        assertEquals(TheInterface3User.TEXT, provider.get(TheInterface3User.class).getText());
    }
    
    @DefaultImplementation(value="directget.get.TheClassThatDoesNotExist")
    @DefaultToNull
    public static interface TheInterface4 {
        
        public String getText();
        
    }
    
    public static class TheInterface4User {
        
        public static final String TEXT = "I am TheInterface4User.";
        
        private TheInterface4 i4;
        
        public TheInterface4User(TheInterface4 i4) {
            this.i4 = i4;
        }
        
        public String getText() {
            return nullable(this.i4).map(TheInterface4::getText).orElse(TEXT);
        }
        
    }
    
    @Test
    public void testThat_whenAnnotatedWithDefaultImplementation_findTheClassAndUseItsDefaultAsThis_nullWhenNotExistAsDefaultToNull() {
        assertEquals(TheInterface4User.TEXT, provider.get(TheInterface4User.class).getText());
    }
    
}
