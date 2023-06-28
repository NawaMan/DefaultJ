//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
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
        
        Object          newInstance = providerClass.getConstructor().newInstance();
        IProvideDefault provider    = IProvideDefault.class.cast(newInstance);
        return provider;
    }
    
    static Optional<IProvideDefault> loadDefault() {
        String requireClassName = System.getProperty(IProvideDefault.implementationClassNameProperty);
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
            String fallbackToBasicDefaultProvider = System.getProperty(IProvideDefault.fallbackToBasicDefaultProvider);
            if ("false".equalsIgnoreCase(fallbackToBasicDefaultProvider))
                return Optional.empty();
            
            BasicDefaultProvider basicProvider = new BasicDefaultProvider();
            return Optional.of(basicProvider);
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
