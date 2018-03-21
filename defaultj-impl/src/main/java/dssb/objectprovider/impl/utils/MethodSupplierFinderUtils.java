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
package dssb.objectprovider.impl.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import dssb.objectprovider.api.IProvideObject;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.NullableJ;
import nawaman.nullablej.nullable.Nullable;

/**
 * Abstract class for supplier finders that get value from constructor or method.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class MethodSupplierFinderUtils {
    
    /**
     * Returns the parameters for the given method.
     * 
     * @param method          the method.
     * @param objectProvider  the object provider to use.
     * @return  the array of parameters.
     */
    public static Object[] prepareParameters(Method method, IProvideObject objectProvider) {
        val paramsArray = method.getParameters();
        val paramValues = getParameters(paramsArray , objectProvider);
        return paramValues;
    }
    
    /**
     * Returns the parameters for the given constructor.
     * 
     * @param constructor     the constructor.
     * @param objectProvider  the object provider to use.
     * @return  the array of parameters.
     */
    public static Object[] prepareParameters(Constructor<?> constructor, IProvideObject objectProvider) {
        val paramsArray = constructor.getParameters();
        val paramValues = getParameters(paramsArray , objectProvider);
        return paramValues;
    }
    
    private static Object[] getParameters(Parameter[] paramsArray, IProvideObject objectProvider) {
        val params = new Object[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            val param             = paramsArray[i];
            val paramType         = param.getType();
            val parameterizedType = param.getParameterizedType();
            boolean isNullable    = param.getAnnotations().has("Nullable")
                                 || param.getAnnotations().has("Optional");
            Object  paramValue    = determineParameterValue(paramType, parameterizedType, isNullable, objectProvider);
            params[i] = paramValue;
        }
        return params;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Object determineParameterValue(Class paramType, Type type, boolean canBeNull, IProvideObject objectProvider) {
        if (type instanceof ParameterizedType) {
            val parameterizedType = (ParameterizedType)type;
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            
            if (Supplier.class.isAssignableFrom(paramType))
                return  (Supplier)(()-> {
                    val value = objectProvider.get(actualType);
                    return value;
                });
            
            if (java.util.function.Supplier.class.isAssignableFrom(paramType))
                return (java.util.function.Supplier)(()->{
                    val value = objectProvider.get(actualType);
                    return value;
                });
            
            val isOptional = Optional.class.isAssignableFrom(paramType);
            val isNullable = !isOptional && Nullable.class.isAssignableFrom(paramType);
            if (isOptional || isNullable) {
                return getNullableOrOptionalValue(canBeNull, objectProvider, actualType, isOptional);
            }
        }
        
        if (canBeNull)
            return getValueOrNullWhenFail(paramType, objectProvider);
        
        val value = objectProvider.get(paramType);
        return value;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Object getNullableOrOptionalValue(boolean canBeNull, IProvideObject objectProvider,
            final java.lang.Class actualType, final boolean isOptional) {
        java.util.function.Function noException   = isOptional ? Optional::ofNullable : Nullable::of;
        java.util.function.Supplier withException = isOptional ? Optional::empty      : Nullable::empty;
        try {
            val paramValue = objectProvider.get(actualType);
            return noException.apply(paramValue);
        } catch (Exception e) {
            return canBeNull ? null : withException.get();
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object getValueOrNullWhenFail(Class paramType, IProvideObject objectProvider) {
        try {
            return objectProvider.get(paramType);
        } catch (Exception e) {
            return null;
        }
    }
    
}
