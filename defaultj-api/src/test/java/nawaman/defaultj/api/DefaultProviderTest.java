//  ========================================================================
//  Copyright (c) 2017-2018 Nawapunth Manusitthipol (NawaMan).
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
package nawaman.defaultj.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

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
                "nawaman.defaultj.api.BasicDefaultProvider",
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
