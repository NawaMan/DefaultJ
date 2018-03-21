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
package nawaman.defaultj.impl.strategies;

import java.util.function.Predicate;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.defaultj.api.IProvideObject;
import nawaman.defaultj.impl.exception.ObjectCreationException;
import nawaman.defaultj.impl.utils.AnnotationUtils;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.NullableJ;

/**
 * This class return default enum value.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class EnumValueSupplierFinder implements IFindSupplier {
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>    theGivenClass,
            IProvideObject objectProvider) {
        if (!theGivenClass.isEnum()) 
            return null;
        
        val enumValue = findDefaultEnumValue(theGivenClass);
        return ()->enumValue;
    }
    
    private static <T> T findDefaultEnumValue(Class<T> theGivenClass) {
        T[] enumConstants = theGivenClass.getEnumConstants();
        if (enumConstants._isEmpty())
            return null;
        
        T enumConstant
                = enumConstants
                ._find(defaultEnumValue(theGivenClass))
                .orElse(enumConstants[0]);
        return enumConstant;
    }
    
    @SuppressWarnings("rawtypes")
    static <T> Predicate<T>defaultEnumValue(Class<T> theGivenClass) {
        return value->{
            val name = ((Enum)value).name();
            try {
                return theGivenClass.getField(name).getAnnotations().has("Default");
            } catch (NoSuchFieldException | SecurityException e) {
                throw new ObjectCreationException(theGivenClass, e);
            }
        };
    }
    
}
