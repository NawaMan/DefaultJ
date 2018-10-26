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

import static nawaman.defaultj.core.utils.AnnotationUtils.has;
import static nawaman.nullablej.NullableJ._isEmpty;

import java.util.function.Predicate;

import lombok.val;
import nawaman.defaultj.annotations.Default;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.exception.DefaultCreationException;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.NullableJ;

/**
 * This class return default enum value.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class EnumValueSupplierFinder implements IFindSupplier {
    
    private static final String DEFAULT = Default.class.getSimpleName();
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        if (!theGivenClass.isEnum()) 
            return null;
        
        val enumValue = findDefaultEnumValue(theGivenClass);
        return ()->enumValue;
    }
    
    private static <T> T findDefaultEnumValue(Class<T> theGivenClass) {
        T[] enumConstants = theGivenClass.getEnumConstants();
        if (_isEmpty(enumConstants))
            return null;
        
        T enumConstant
                = NullableJ._find(enumConstants, defaultEnumValue(theGivenClass))
                .orElse(enumConstants[0]);
        return enumConstant;
    }
    
    @SuppressWarnings("rawtypes")
    static <T> Predicate<T>defaultEnumValue(Class<T> theGivenClass) {
        return value->{
            val name = ((Enum)value).name();
            try {
                return has(theGivenClass.getField(name).getAnnotations(), DEFAULT);
            } catch (NoSuchFieldException | SecurityException e) {
                throw new DefaultCreationException(theGivenClass, e);
            }
        };
    }
    
}
