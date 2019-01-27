//  ========================================================================
//  Copyright (c) 2017-2019 Nawapunth Manusitthipol (NawaMan).
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

/**
 * This default provider can only create a new instance from default constructor.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class BasicDefaultProvider implements IProvideDefault {
    
    @Override
    public <TYPE> TYPE get(Class<TYPE> theGivenClass) throws ProvideDefaultException {
        try {
            return (TYPE)theGivenClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ProvideDefaultException("BasicDefaultProvider requires a default constructor.", theGivenClass, e);
        }
    }
    
}
