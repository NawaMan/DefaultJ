package nawaman.defaultj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A ready to use annotation to specify that the method should be run after constructor is called by DefaultJ.
 * NOTE: This is only done if the object is created by ConstructorSupplierFinder.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostConstruct {
    
}
