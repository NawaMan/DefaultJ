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
package dssb.objectprovider.api;

import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * The tests to get the default provider.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class DefaultProviderTest {
    
    /**
     * This test assert that the default provider is null
     *   as we know that the implementation class is not in the class path.
     */
    @Test
    public void testThatWhenNoImplementationInTheClassPath_defaultProviderIsNull() {
        assertNull(IProvideObject.defaultProvider().orElse(null));
    }
    
}
