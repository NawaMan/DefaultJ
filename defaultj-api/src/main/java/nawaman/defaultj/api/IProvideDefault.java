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

import java.util.Optional;

/**
 * Classes implementing this interface can provide a default given a class..
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@FunctionalInterface
public interface IProvideDefault {
    
    /**
     * Returns the default for the given class.
     * 
     * @param <TYPE>  the data type represented by the given class.
     * @param theGivenClass
     *          the given class.
     * @return  the default associated with the given class.
     * @throws ProvideDefaultException  if there is a problem getting the default.
     */
    public <TYPE> TYPE get(Class<TYPE> theGivenClass)
            throws ProvideDefaultException;
    
    
    // == Factory method ==
    
    /** The name of the class of the implementation. */
    public static final String implementationClassName = "nawaman.defaultj.core.DefaultProvider";
    
    /**
     * Attempt to find the default provider.
     * 
     * This is done by looking for the class DefaultProvider from the core package by name.
     * If the class is not in the classpath. This method will return null.
     * 
     * @return the Optional value of the DefaultProvider if the class was in the classpath.
     */
    public static Optional<IProvideDefault> defaultProvider() {
        Class<?> providerClass      = utils.findClass(implementationClassName);
        boolean  isClassInClasspath = providerClass != null;
        if (!isClassInClasspath)
            return Optional.empty();
        
        boolean isCompatibleType = IProvideDefault.class.isAssignableFrom(providerClass);
        if (!isCompatibleType) 
            return Optional.empty();
        
        try {
            Object          newInstance = providerClass.newInstance();
            IProvideDefault provider    = IProvideDefault.class.cast(newInstance);
            return Optional.ofNullable(provider);
            
        } catch (InstantiationException | IllegalAccessException e) {
            return Optional.empty();
        }
    }
    
    
}