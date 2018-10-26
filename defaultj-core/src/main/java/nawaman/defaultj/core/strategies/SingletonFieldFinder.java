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

import static java.util.Arrays.stream;
import static nawaman.defaultj.core.utils.FieldUtils.annotatedWith;
import static nawaman.defaultj.core.utils.FieldUtils.ifPublicField;
import static nawaman.defaultj.core.utils.FieldUtils.ifStaticField;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import lombok.Value;
import lombok.val;
import nawaman.defaultj.annotations.Default;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.nullable.Nullable;

/**
 * This class provides value from the singleton field
 * 
 * <ul>
 *  <li>the static final field</li>
 *  <li>the same type, the Optional of the type, the Nullable of the type or the Supplier of the type</li>
 *  <li>annotated with @Default</li>
 * </ul>
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class SingletonFieldFinder implements IFindSupplier {
    
    private static final String            DEFAULT              = Default.class.getSimpleName();
    private static final Predicate<Field>  annotatedWithDefault = annotatedWith(DEFAULT);
    private static final Predicate<Object> notNull              = Objects::nonNull;

    @SuppressWarnings({ "unchecked" })
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        Supplier<TYPE, THROWABLE> fieldValue = findValueFromSingletonField(theGivenClass);
        return fieldValue;
    }
    
    @SuppressWarnings("rawtypes")
    private static <T> Supplier findValueFromSingletonField(Class<T> theGivenClass) {
        val helper = new Helper<T>(theGivenClass);
        return (Supplier)stream(theGivenClass.getDeclaredFields())
                .filter(ifPublicField)
                .filter(ifStaticField)
                .filter(annotatedWithDefault)
                .map(helper::findValue)
                .filter(notNull)
                .findAny()
                .orElse(null);
    }
    
    @Value
    static class Helper<T> {
        private Class<T> theGivenClass;
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Supplier findValue(Field field) {
            val type = field.getType();
            if (theGivenClass.isAssignableFrom(type))
                return (Supplier)(()->getFieldValue(field));
            
            val optionalSupplier = findOptionalOrNullableFieldValue(field, type);
            if (optionalSupplier != null)
                return optionalSupplier;
            
            val supplierSupplier = findSupplierFieldValue(field, type);
            if (supplierSupplier != null)
                return supplierSupplier;
            
            return null;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private Supplier findSupplierFieldValue(Field field, final java.lang.Class<?> type) {
            if (!java.util.function.Supplier.class.isAssignableFrom(type))
                return null;
            
            val parameterizedType = (ParameterizedType)field.getGenericType();
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            val supplier = (Supplier)()->{ 
                val value = ((java.util.function.Supplier)getFieldValue(field)).get();
                return value;
            };
            return supplier;
        }
        
        private Object getFieldValue(Field field) throws IllegalAccessException {
            return field.get(theGivenClass);
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private Supplier findOptionalOrNullableFieldValue(Field field, final java.lang.Class<?> type) {
            boolean isOptional = Optional.class.isAssignableFrom(type);
            boolean isNullable = !isOptional && Nullable.class.isAssignableFrom(type);
            if (!isOptional && !isNullable)
                return null;
            
            val parameterizedType = (ParameterizedType)field.getGenericType();
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            val supplier = (Supplier)(()-> {
                val optional = getFieldValue(field);
                val value
                        = isOptional
                        ? ((Optional)optional).orElse(null)
                        : ((Nullable)optional).orElse(null);
                return value;
            });
            return supplier;
        }
    }
    
}
