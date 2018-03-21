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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dssb.objectprovider.api.IProvideObject;

@SuppressWarnings("javadoc")
public class DefaultProviderTest {
    
    @Test
    public void testDefaultProvider() {
        assertTrue(ObjectProvider.class.isInstance(IProvideObject.defaultProvider().get()));
    }
    
}
