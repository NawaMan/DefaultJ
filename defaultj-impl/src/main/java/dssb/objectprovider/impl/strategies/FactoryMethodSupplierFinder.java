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
package dssb.objectprovider.impl.strategies;

import static dssb.objectprovider.impl.utils.MethodSupplierFinderUtils.prepareParameters;
import static dssb.objectprovider.impl.utils.MethodUtils.annotatedWith;
import static dssb.objectprovider.impl.utils.MethodUtils.ifPublicMethod;
import static dssb.objectprovider.impl.utils.MethodUtils.ifStaticMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Predicate;

import dssb.objectprovider.api.IProvideObject;
import dssb.objectprovider.impl.utils.AnnotationUtils;
import lombok.Value;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.NullableJ;
import nawaman.nullablej.nullable.Nullable;

/**
 * This class returns object resulting from a factory method.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class FactoryMethodSupplierFinder implements IFindSupplier {
    
    private static final Predicate<Method> annotatedWithDefault = annotatedWith("Default");
    
    @SuppressWarnings({ "unchecked" })
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>    theGivenClass,
            IProvideObject objectProvider) {
        val methodValue = findValueFromFactoryMethod(theGivenClass, objectProvider);
        return (Supplier<TYPE, THROWABLE>)methodValue;
    }
    
    @SuppressWarnings("unchecked")
    private <T> Supplier<T, ? extends Throwable> findValueFromFactoryMethod(Class<T> theGivenClass, IProvideObject objectProvider) {
        val helper   = new Helper<T>(theGivenClass, objectProvider);
        val supplier = (Supplier<T, ? extends Throwable>)
                theGivenClass.getDeclaredMethods()._stream$()
                .filter(ifStaticMethod)
                .filter(ifPublicMethod)
                .filter(annotatedWithDefault)
                .map(helper::findValue)
                .findAny()
                .orElse(null);
        return supplier;
    }
    
    @Value
    static class Helper<T> {
        private Class<T> theGivenClass;
        private IProvideObject objectProvider;
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private Supplier findValue(Method method) {
            val type = method.getReturnType();
            if (theGivenClass.isAssignableFrom(type))
                return (Supplier)(()->basicFactoryMethodCall(method));
            
            val fromNullable = findNullableOrOptional(method, type);
            if (fromNullable._isNotNull())
                return fromNullable;
            
            val fromSupplier = findSupplier(method, type);
            if (fromSupplier._isNotNull())
                return fromSupplier;
            
            return null;
        }
        
        @SuppressWarnings("rawtypes")
        private Supplier findSupplier(Method method, final java.lang.Class<?> type) {
            if (!java.util.function.Supplier.class.isAssignableFrom(type)) 
                return null;
            
            val parameterizedType = (ParameterizedType)method.getGenericReturnType();
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            val getMethod = getGetMethod();
             return (Supplier)(()->supplierFactoryMethodCall(method, getMethod));
        }
        
        @SuppressWarnings("rawtypes")
        private Supplier findNullableOrOptional(Method method, Class<?> type) {
            val isOptional = Optional.class.isAssignableFrom(type);
            val isNullable = !isOptional && Nullable.class.isAssignableFrom(type);
            if (!isOptional && !isNullable)
                return null;
            
            val parameterizedType = (ParameterizedType)method.getGenericReturnType();
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            return (Supplier)(()->getNullableOrOptionalValue(method, isNullable));
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private Object getNullableOrOptionalValue(Method method, final boolean isNullable)
                throws IllegalAccessException, InvocationTargetException {
            val params   = prepareParameters(method, objectProvider);
            val nullable = method.invoke(theGivenClass, params);
            val value = isNullable
                    ? ((Nullable)nullable).orElse(null)
                    : ((Optional)nullable).orElse(null);
            return value;
        }
        
        private static Method getGetMethod() {
            try {
                // TODO - Change to use MethodHandler.
                return java.util.function.Supplier.class.getMethod("get", new Class[0]);
            } catch (NoSuchMethodException | SecurityException e) {
                // I am sure it is there.
                throw new RuntimeException(e);
            }
        }
        
        private Object supplierFactoryMethodCall(
                Method method,
                Method getMethod) 
                        throws IllegalAccessException, InvocationTargetException {
            val params = prepareParameters(method, objectProvider);
            val result = method.invoke(theGivenClass, params);
            val value  = getMethod.invoke(result);
            return value;
        }
        
        private Object basicFactoryMethodCall(Method method)
                throws IllegalAccessException, InvocationTargetException {
            val params = prepareParameters(method, objectProvider);
            val value  = method.invoke(theGivenClass, params);
            return value;
        }
    }
    
}
