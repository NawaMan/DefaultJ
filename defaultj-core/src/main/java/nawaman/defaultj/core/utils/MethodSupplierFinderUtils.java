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
package nawaman.defaultj.core.utils;

import static nawaman.defaultj.core.utils.AnnotationUtils.has;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import lombok.val;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.nullable.Nullable;

/**
 * Abstract class for supplier finders that get value from constructor or method.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class MethodSupplierFinderUtils {
    
    /**
     * Returns the parameters for the given method.
     * 
     * @param method           the method.
     * @param defaultProvider  the default provider to use.
     * @return  the array of parameters.
     */
    public static Object[] prepareParameters(Method method, IProvideDefault defaultProvider) {
        val paramsArray = method.getParameters();
        val paramValues = getParameters(paramsArray , defaultProvider);
        return paramValues;
    }
    
    /**
     * Returns the parameters for the given constructor.
     * 
     * @param constructor     the constructor.
     * @param defaultProvider  the object provider to use.
     * @return  the array of parameters.
     */
    public static Object[] prepareParameters(Constructor<?> constructor, IProvideDefault defaultProvider) {
        val paramsArray = constructor.getParameters();
        val paramValues = getParameters(paramsArray , defaultProvider);
        return paramValues;
    }
    
    private static Object[] getParameters(Parameter[] paramsArray, IProvideDefault defaultProvider) {
        val params = new Object[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            val param             = paramsArray[i];
            val paramType         = param.getType();
            val parameterizedType = param.getParameterizedType();
            boolean isNullable    = has(param.getAnnotations(), "Nullable")
                                 || has(param.getAnnotations(), "Optional");
            Object  paramValue    = determineParameterValue(paramType, parameterizedType, isNullable, defaultProvider);
            params[i] = paramValue;
        }
        return params;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Object determineParameterValue(Class paramType, Type type, boolean canBeNull, IProvideDefault defaultProvider) {
        if (type instanceof ParameterizedType) {
            val parameterizedType = (ParameterizedType)type;
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            
            if (Supplier.class.isAssignableFrom(paramType))
                return  (Supplier)(()-> {
                    val value = defaultProvider.get(actualType);
                    return value;
                });
            
            if (java.util.function.Supplier.class.isAssignableFrom(paramType))
                return (java.util.function.Supplier)(()->{
                    val value = defaultProvider.get(actualType);
                    return value;
                });
            
            val isOptional = Optional.class.isAssignableFrom(paramType);
            val isNullable = !isOptional && Nullable.class.isAssignableFrom(paramType);
            if (isOptional || isNullable) {
                return getNullableOrOptionalValue(canBeNull, defaultProvider, actualType, isOptional);
            }
        }
        
        if (canBeNull)
            return getValueOrNullWhenFail(paramType, defaultProvider);
        
        val value = defaultProvider.get(paramType);
        return value;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Object getNullableOrOptionalValue(boolean canBeNull, IProvideDefault defaultProvider,
            final java.lang.Class actualType, final boolean isOptional) {
        java.util.function.Function noException   = isOptional ? Optional::ofNullable : Nullable::of;
        java.util.function.Supplier withException = isOptional ? Optional::empty      : Nullable::empty;
        try {
            val paramValue = defaultProvider.get(actualType);
            return noException.apply(paramValue);
        } catch (Exception e) {
            return canBeNull ? null : withException.get();
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object getValueOrNullWhenFail(Class paramType, IProvideDefault defaultProvider) {
        try {
            return defaultProvider.get(paramType);
        } catch (Exception e) {
            return null;
        }
    }
    
}
