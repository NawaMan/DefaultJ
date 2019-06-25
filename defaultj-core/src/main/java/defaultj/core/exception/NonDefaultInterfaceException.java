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
package defaultj.core.exception;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * This exception is thrown when a method is called when a default interface
 *   as asked but there is at least one method that is not .
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NonDefaultInterfaceException extends AbstractClassCreationException {
    
    private static final long serialVersionUID = -8000862544457872776L;
    
    private final Map<String, String> methods;
    
    /**
     * Constructor 
     * 
     * @param clzz  the class.
     * @param methods the methods,
     **/
    public NonDefaultInterfaceException(Class<?> clzz, Map<String, String> methods) {
        super("Non-default method found in '" + clzz + "': " + methods, clzz);
        this.methods = unmodifiableMap((methods != null) ? methods : new HashMap<String, String>());
    }
    
    /** @return the method with the problem. */
    public Map<String, String> getMethod() {
        return methods;
    }
}
