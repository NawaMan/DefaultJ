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

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
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
            return handleDefaultInterface(theGivenClass, hashCode, proxy, getDefaultMethod(method), args);
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
        if ("toString".equals(getDefaultMethod(method).getName()) && (getDefaultMethod(method).getParameterCount() == 0))
            return theGivenClass.getSimpleName() + "@" + hashCode;
        if ("hashCode".equals(getDefaultMethod(method).getName()) && (getDefaultMethod(method).getParameterCount() == 0))
            return hashCode;
        if ("equals".equals(getDefaultMethod(method).getName()) && (getDefaultMethod(method).getParameterCount() == 1)) {
            return this == args[0];
        }
        
        return invokeDefaultMethod(proxy, getDefaultMethod(method), args);
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws NoSuchMethodException,
            Throwable, IllegalAccessException, InstantiationException, InvocationTargetException {
        val defaultMethod = getDefaultMethod(method);
        
        // TODO - See if we can avoid this in case it is already done.
        val declaringClass = defaultMethod.getDeclaringClass();
        val constructor    = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        
        val result = constructor
                .newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(defaultMethod, declaringClass)
                .bindTo(proxy)
                .invokeWithArguments(args);
        return result;
    }

    private Method getDefaultMethod(Method method) {
        if (method.isDefault())
            return method;
        
        for (Class<?> superInterface : method.getDeclaringClass().getInterfaces()) {
            Method defaultMethod = getDefaultMethodFrom(method, superInterface);
            if (defaultMethod != null)
                return defaultMethod;
        }
        throw new NonDefaultMethodException(method);
    }
    
    private Method getDefaultMethodFrom(Method method, Class<?> thisInterface) {
        try {
            Method foundMethod = thisInterface.getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (foundMethod.isDefault())
                return foundMethod;
        } catch (NoSuchMethodException | SecurityException e) {
            
        }
        
        for (Class<?> superInterface : thisInterface.getInterfaces()) {
            Method defaultMethod = getDefaultMethodFrom(method, superInterface);
            if (defaultMethod != null)
                return defaultMethod;
        }
        
        return null;
    }

    @AllArgsConstructor
    private static class InterfaceChecker<T> {
        
        private Class<T> orgInterface;
        
        private final Map<String, String> abstracts = new TreeMap<String, String>();
        
        private final Set<String> defaults = new TreeSet<String>();
        
        private Map<String, String> ensureDefaultInterface() {
            ensureDefaultInterface(orgInterface);
            defaults.forEach(m -> abstracts.remove(m));
            return abstracts;
        }
        
        private void ensureDefaultInterface(Class<?> element) {
            for (Method method : element.getDeclaredMethods()) {
                if (method.isDefault() || !java.lang.reflect.Modifier.isAbstract(method.getModifiers()))
                     defaults.add(methodSignature(method));
                else abstracts.put(methodSignature(method), element.getCanonicalName());
            }
            
            for (Class<?> intf : element.getInterfaces()) {
                ensureDefaultInterface(intf);
            }
        }
        
        private static String methodSignature(Method method) {
            return method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + "): " + method.getReturnType();
        }
    }
    
}
