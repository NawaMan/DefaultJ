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
package defaultj.core.strategies;

import static defaultj.core.utils.FieldUtils.annotatedWith;
import static defaultj.core.utils.FieldUtils.ifFinalField;
import static defaultj.core.utils.FieldUtils.ifPublicField;
import static defaultj.core.utils.FieldUtils.ifStaticField;
import static java.util.Arrays.stream;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import defaultj.annotations.Default;
import defaultj.api.IProvideDefault;
import defaultj.core.utils.failable.Failable.Supplier;
import lombok.val;
import nullablej.nullable.Nullable;

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
                .filter(ifFinalField)
                .filter(annotatedWithDefault)
                .map   (helper::findValue)
                .filter(notNull)
                .findAny()
                .orElse(null);
    }
    
    static class Helper<T> {
        private Class<T> theGivenClass;
        
        public Helper(Class<T> theGivenClass) {
            this.theGivenClass = theGivenClass;
        }
        
        @SuppressWarnings({ "rawtypes" })
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
        
        @SuppressWarnings({ "rawtypes" })
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
                val value = isOptional
                          ? ((Optional)optional).orElse(null)
                          : ((Nullable)optional).orElse(null);
                return value;
            });
            return supplier;
        }
    }
    
}
