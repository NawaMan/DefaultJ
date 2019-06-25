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
package defaultj.core;

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;

/**
 * Collections of bindings.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
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
            if (binding != null)
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
            if (bindings != null)
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
