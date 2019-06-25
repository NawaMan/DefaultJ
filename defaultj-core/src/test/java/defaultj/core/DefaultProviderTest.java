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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Test;

import defaultj.api.IProvideDefault;
import defaultj.core.DefaultProvider;

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
