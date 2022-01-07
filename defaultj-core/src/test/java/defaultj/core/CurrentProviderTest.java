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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import defaultj.core.bindings.TypeBinding;

@SuppressWarnings("javadoc")
public class CurrentProviderTest {
    
    public static interface Greet {
        public String greet();
    }
    
    public static class Hello implements Greet {
        @Override
        public String greet() {
            return "Hello";
        }
    }
    public static class Hi extends Hello {
        @Override
        public String greet() {
            return "Hi";
        }
    }
    
    @Test
    public void testThatSubsequnceGetStillWithTheSameProvider() {
        var bindings = new Bindings.Builder()
            .bind(Greet.class, new TypeBinding<Hello>(Hello.class))
            .bind(Hello.class, new TypeBinding<Hello>(Hi.class))
            .build();
        var provider = DefaultProvider.instance.wihtBindings(bindings);
        assertEquals("Hi", provider.get(Greet.class).greet());
    }
    
}
