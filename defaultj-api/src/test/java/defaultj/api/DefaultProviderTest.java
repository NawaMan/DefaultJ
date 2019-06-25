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
package defaultj.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import defaultj.api.IProvideDefault;
import defaultj.api.RequiredDefaultProviderNotAvailableException;
import defaultj.api.utils;

/**
 * The tests to get the default provider.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultProviderTest {
    
    /**
     * This test assert that the default provider is null
     *   as we know that the implementation class is not in the class path.
     */
    @Test
    public void testThatWhenNoImplementationInTheClassPath_fallbackToBasic() {
        assertEquals(
                "defaultj.api.BasicDefaultProvider",
                IProvideDefault.defaultProvider()
                .map(Object::getClass)
                .map(Class ::getCanonicalName)
                .orElse(null));
    }
    
    @Test
    public void testThatWhenNoImplementationInTheClassPath_notFallBack_defaultToNull() {
        try {
            System.setProperty(IProvideDefault.fallbackToBasicDefaultProvider, "false");
            assertNull(IProvideDefault.defaultProvider().orElse(null));
        } finally {
            System.setProperty(IProvideDefault.fallbackToBasicDefaultProvider, "");
        }
    }
    
    @Test
    public void testSuggestImplementation() {
        try {
            utils.cachedProvider.set(null);
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, SuggestedDefaultProvider.class.getCanonicalName());
            assertTrue(IProvideDefault.defaultProvider().get() instanceof SuggestedDefaultProvider);
        } finally {
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, "");
            utils.cachedProvider.set(null);
        }
    }
    
    @Test
    public void testSuggestImplementation_notExist() {
        try {
            utils.cachedProvider.set(null);
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, "non.exist." + SuggestedDefaultProvider.class.getCanonicalName());
            System.setProperty(IProvideDefault.fallbackToBasicDefaultProvider        , "false");
            assertFalse(IProvideDefault.defaultProvider().isPresent());
        } finally {
            System.setProperty(IProvideDefault.fallbackToBasicDefaultProvider        , "");
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, "");
            utils.cachedProvider.set(null);
        }
    }
    
    @Test
    public void testRequiredImplementation() {
        try {
            utils.cachedProvider.set(null);
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, SuggestedDefaultProvider.class.getCanonicalName());
            System.setProperty(IProvideDefault.implementationClassNameProperty, RequiredDefaultProvider.class.getCanonicalName());
            assertTrue(IProvideDefault.defaultProvider().get() instanceof RequiredDefaultProvider);
        } finally {
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, "");
            System.setProperty(IProvideDefault.implementationClassNameProperty,        "");
            utils.cachedProvider.set(null);
        }
    }
    
    @Test
    public void testRequiredImplementation_notExist() {
        try {
            utils.cachedProvider.set(null);
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, SuggestedDefaultProvider.class.getCanonicalName());
            System.setProperty(IProvideDefault.implementationClassNameProperty, "non.exist." + RequiredDefaultProvider.class.getCanonicalName());
            IProvideDefault.defaultProvider();
            fail("Expect a failure");
        } catch (RequiredDefaultProviderNotAvailableException e) {
            // Expect!
        } finally {
            System.setProperty(IProvideDefault.suggestImplementationClassNameProperty, "");
            System.setProperty(IProvideDefault.implementationClassNameProperty,        "");
            utils.cachedProvider.set(null);
        }
    }
    
}
