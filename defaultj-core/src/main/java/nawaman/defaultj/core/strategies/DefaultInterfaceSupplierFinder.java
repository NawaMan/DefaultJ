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
package nawaman.defaultj.core.strategies;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.defaultj.annotations.DefaultInterface;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.exception.NonDefaultInterfaceException;
import nawaman.defaultj.core.exception.NonDefaultMethodException;
import nawaman.defaultj.core.utils.AnnotationUtils;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.NullableJ;
import nawaman.nullablej._internal.UReflection;

/**
 * This class find default of an interface with all default methods.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class DefaultInterfaceSupplierFinder implements IFindSupplier {

    private static final String DEFAULT_INTERFACE = DefaultInterface.class.getSimpleName();
    private static final Random random = new Random();
    
    @SuppressWarnings("unchecked")
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        boolean isDefaultInterface
                =  theGivenClass.isInterface()
                && theGivenClass.getAnnotations().has(DEFAULT_INTERFACE);
        
        if (!isDefaultInterface)
            return null;
        
        val nonDefaults = new InterfaceChecker<TYPE>(theGivenClass).ensureDefaultInterface();
        if (!nonDefaults.isEmpty()) {
            throw new NonDefaultInterfaceException(theGivenClass, nonDefaults);
        }
        
        val classLoader = theGivenClass.getClassLoader();
        val interfaces  = new Class<?>[] { theGivenClass };
        val hashCode    = Math.abs(random.nextInt() / 2);
        val theProxy    = (TYPE)Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args)->{
            return handleDefaultInterface(theGivenClass, hashCode, proxy, method, args);
        });
        return () -> theProxy;
    }
    
    private <TYPE> Object handleDefaultInterface(
            Class<TYPE> theGivenClass,
            int hashCode,
            Object proxy,
            Method method,
            Object[] args)
                    throws Throwable {
        // TODO Redirect this somewhere.
        if ("toString".equals(method.getName()) && (method.getParameterCount() == 0))
            return theGivenClass.getSimpleName() + "@" + hashCode;
        if ("hashCode".equals(method.getName()) && (method.getParameterCount() == 0))
            return hashCode;
        if ("equals".equals(method.getName()) && (method.getParameterCount() == 1)) {
            return this == args[0];
        }
        
        if (!method.isDefault())
            throw new NonDefaultMethodException(method);
        
        return UReflection.invokeDefaultMethod(proxy, method, args);
    }
    
    @AllArgsConstructor
    static class InterfaceChecker<T> {
        
        private Class<T> orgInterface;
        
        private final Map<String, String> abstracts = new TreeMap<String, String>();
        
        private final Set<String> defaults = new TreeSet<String>();
        
        Map<String, String> ensureDefaultInterface() {
            ensureDefaultInterface(orgInterface);
            defaults.forEach(m -> abstracts.remove(m));
            return abstracts;
        }
        
        void ensureDefaultInterface(Class<?> element) {
            for (Method method : element.getDeclaredMethods()) {
                if (method.isDefault() || !java.lang.reflect.Modifier.isAbstract(method.getModifiers()))
                     defaults.add(methodSignature(method));
                else abstracts.put(methodSignature(method), element.getCanonicalName());
            }
            
            for (Class<?> intf : element.getInterfaces()) {
                ensureDefaultInterface(intf);
            }
        }
        
        static String methodSignature(Method method) {
            return method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + "): " + method.getReturnType();
        }
    }
    
}
