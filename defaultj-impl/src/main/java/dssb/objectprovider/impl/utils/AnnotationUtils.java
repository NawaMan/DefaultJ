package dssb.objectprovider.impl.utils;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

/**
 * Utility class for Annotations.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@ExtensionMethod({
    NullableJ.class
})
public class AnnotationUtils {

    /**
     * Check if the an annotation is has the simple name as the one given 
     * @param name  the name expected.
     * @return  the predicate to check if annotation is with the given name.
     **/
    public static Predicate<? super Annotation> withNamed(String name) {
        return annotation->{
            val toString = annotation.toString();
            return toString.matches("^@.*(\\.|\\$)" + name + "\\(.*$");
        };
    }
    
    /**
     * Check to see if the given array of annotation has at least one of the given name.
     * 
     * @param annotations  an array of annotations.
     * @param names        the names expected.
     * @return  {@code true}  if the annotations has at lease one of the names.
     */
    public static boolean has(Annotation[] annotations, String ... names) {
        return names._stream$().anyMatch(
                name -> annotations._hasSome(withNamed(name)));
    }
    
    /**
     * Check to see if the given array of annotation has all of the specified names.
     * 
     * @param annotations  an array of annotations.
     * @param names        the names expected.
     * @return  {@code true}  if the annotations has at lease one of the names.
     */
    public static boolean hasAllOf(Annotation[] annotations, String ... names) {
        return names._stream$().allMatch(
                name -> annotations._hasAll(withNamed(name)));
    }
    
}
