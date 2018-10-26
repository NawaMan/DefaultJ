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
import java.util.concurrent.atomic.AtomicReference;

class utils {
    
    static final AtomicReference<Optional<IProvideDefault>> cachedProvider = new AtomicReference<>(null);
    
    static Class<?> findClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    static IProvideDefault loadDefaultByName(String className) throws Exception {
        Class<?> providerClass      = utils.findClass(className);
        boolean  isClassInClasspath = providerClass != null;
        if (!isClassInClasspath)
            throw new ClassNotFoundException(className);
        
        boolean isCompatibleType = IProvideDefault.class.isAssignableFrom(providerClass);
        if (!isCompatibleType) 
            throw new ClassCastException(className);
        
        Object          newInstance = providerClass.newInstance();
        IProvideDefault provider    = IProvideDefault.class.cast(newInstance);
        return provider;
    }
    
    static Optional<IProvideDefault> loadDefault() {
        String requireClassName = System.getProperty(IProvideDefault.implementationClassNameProperty);
        System.out.println("requireClassName: " + requireClassName);
        if ((requireClassName != null) && !requireClassName.trim().isEmpty()) {
            try {
                IProvideDefault requireProvider = utils.loadDefaultByName(requireClassName);
                return Optional.of(requireProvider);
            } catch (Exception e) {
                System.err.println("Required DefaultProvider does not exist or fail to load: " + requireClassName);
                e.printStackTrace();
                throw new RequiredDefaultProviderNotAvailableException(e);
            }
        }
        
        String suggestClassName = System.getProperty(IProvideDefault.suggestImplementationClassNameProperty);
        System.out.println("suggestClassName: " + suggestClassName);
        if ((suggestClassName != null) && !suggestClassName.trim().isEmpty()) {
            try {
                IProvideDefault suggestProvider = utils.loadDefaultByName(suggestClassName);
                return Optional.of(suggestProvider);
            } catch (Exception e) {
                System.err.println("Suggest DefaultProvider does not exist or fail to load: " + suggestClassName);
                e.printStackTrace();
            }
        }
        
        try {
            String          className = IProvideDefault.implementationClassName;
            IProvideDefault provider = utils.loadDefaultByName(className);
            return Optional.ofNullable(provider);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    static Optional<IProvideDefault> getDefault() {
        if (cachedProvider.get() != null)
            return cachedProvider.get();
        
        synchronized (utils.class) {
            if (cachedProvider.get() != null)
                return cachedProvider.get();
            
            Optional<IProvideDefault> loaded = loadDefault();
            cachedProvider.compareAndSet(null, loaded);
            
            return cachedProvider.get();
        }
    }
    
}
