//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
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
package defaultj.core.strategies;

import static defaultj.core.utils.AnnotationUtils.has;
import static nullablej.NullableJ._isEmpty;

import java.util.function.Predicate;

import defaultj.annotations.Default;
import defaultj.api.IProvideDefault;
import defaultj.core.exception.DefaultCreationException;
import defaultj.core.utils.failable.Failable.Supplier;
import nullablej.NullableJ;

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
        
        var enumValue = findDefaultEnumValue(theGivenClass);
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
            var name = ((Enum)value).name();
            try {
                return has(theGivenClass.getField(name).getAnnotations(), DEFAULT);
            } catch (NoSuchFieldException | SecurityException e) {
                throw new DefaultCreationException(theGivenClass, e);
            }
        };
    }
    
}
