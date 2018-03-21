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

import java.util.Optional;

/**
 * Classes implementing this interface can provide an object given a class..
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@FunctionalInterface
public interface IProvideObject {
    
    /**
     * Returns the object for the given class.
     * 
     * @param <TYPE>  the data type represented by the given class.
     * @param theGivenClass
     *          the given class.
     * @return  the object associated with the given class.
     * @throws ProvideObjectException  if there is a problem getting the object.
     */
    public <TYPE> TYPE get(Class<TYPE> theGivenClass)
            throws ProvideObjectException;
    
    
    // == Factory method ==
    
    /** The name of the class of the implementation. */
    public static final String implementationClassName = "dssb.objectprovider.impl.ObjectProvider";
    
    /**
     * Attempt to find the default provider.
     * 
     * This is done by looking for the class ObjectProvider from the impl package by name.
     * If the class is not in the classpath. This method will return null.
     * 
     * @return the Optional value of the ObjectProvider if the class was in the classpath.
     */
    public static Optional<IProvideObject> defaultProvider() {
        Class<?> providerClass      = utils.findClass(implementationClassName);
        boolean  isClassInClasspath = providerClass != null;
        if (!isClassInClasspath)
            return Optional.empty();
        
        boolean isCompatibleType = IProvideObject.class.isAssignableFrom(providerClass);
        if (!isCompatibleType) 
            return Optional.empty();
        
        try {
            Object         newInstance = providerClass.newInstance();
            IProvideObject provider    = IProvideObject.class.cast(newInstance);
            return Optional.ofNullable(provider);
            
        } catch (InstantiationException | IllegalAccessException e) {
            return Optional.empty();
        }
    }
    
    
}
