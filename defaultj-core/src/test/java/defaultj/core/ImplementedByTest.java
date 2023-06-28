//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
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

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import defaultj.core.DefaultImplementationTest.DefaultImplementation;
import defaultj.core.exception.ImplementedClassNotCompatibleExistException;

public class ImplementedByTest {

    private DefaultProvider provider = new DefaultProvider();
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ImplementedBy {
        
        public Class<?> value();
        
    }
    
    @ImplementedBy(TheClass.class)
    @DefaultImplementation("non.exist.class")
    public static interface TheInterface {
        
        public String getText();
        
    }
    public static class TheClass implements TheInterface {
        
        public static final String TEXT = "I the class.";
        
        @Override
        public String getText() {
            return TEXT;
        }
        
    }
    
    @Test
    public void testImplementationNameNotExistSoClassIsUsed() {
        assertEquals(TheClass.class, provider.get(TheInterface.class).getClass());
    }
    
    @ImplementedBy(TheClass2.class)
    @DefaultImplementation("defaultj.core.TheClass3")
    public static interface TheInterface2 {
        
        public String getText();
        
    }
    public static class TheClass2 implements TheInterface2 {
        
        public static final String TEXT = "I the class.";
        
        @Override
        public String getText() {
            return TEXT;
        }
        
    }
    
    @Test
    public void testImplementationNameExistSoItIsUsed() {
        assertEquals(TheClass3.class, provider.get(TheInterface2.class).getClass());
    }
    
    @ImplementedBy(Object.class)
    public static interface TheInterface3 {
        
        public String getText();
        
    }
    
    @Test(expected=ImplementedClassNotCompatibleExistException.class)
    public void testImplementationNotCompatible() {
        provider.get(TheInterface3.class);
    }
}
