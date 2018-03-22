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
package nawaman.defaultj.core;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

/**
 * Collections of bindings.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({ NullableJ.class })
public class Bindings {
    
    @SuppressWarnings("rawtypes")
    private final Map<Class, IBind> bindings;
    
    Bindings(@SuppressWarnings("rawtypes") Map<Class, IBind> bindings) {
        this.bindings = unmodifiableMap(new HashMap<>(bindings));
    }
    
    /**
     * Returns the bindings for the given class.
     * 
     * @param <TYPE>  the data type.
     * @param clzz    the data class.
     * @return  the binding for the class.
     */
    @SuppressWarnings("unchecked")
    public <TYPE> IBind<TYPE> getBinding(Class<TYPE> clzz) {
        return (IBind<TYPE>)this.bindings.get(clzz);
    }
    
    /**
     * Creates a binder for the given type.
     */
    public static class Builder {
        
        @SuppressWarnings("rawtypes")
        private final Map<Class, IBind> bindings = new HashMap<>();
        
        /**
         * Constructor with no predefine binders.
         */
        public Builder() {
            
        }
        
        /**
         * Constructs a builder with bingings.
         * 
         * @param bindings  the predefine bindings.
         */
        public Builder(Bindings bindings) {
            if (bindings != null)
                this.bindings.putAll(bindings.bindings);
        }
        
        /**
         * Bind the binding to the class.
         * 
         * @param <TYPE>   the data type.
         * @param clzz     the data class.
         * @param binding  the bind to be match with this class.
         * @return this binding builder.
         */
        public <TYPE> Builder bind(@NonNull Class<TYPE> clzz, IBind<? extends TYPE> binding) {
            if (binding._isNotNull())
                this.bindings.put(clzz, binding);
            return this;
        }
        /**
         * Include all the bindings.
         * 
         * @param bindings  the bindings.
         * @return this binding builder.
         */
        public Builder bind(Bindings bindings) {
            if (bindings._isNotNull())
                this.bindings.putAll(bindings.bindings);
            return this;
        }
        
        /**
         * Build the binding as specified.
         * 
         * @return the newly created binding.
         */
        public Bindings build() {
            return new Bindings(bindings);
        }
        
    }
    
}
