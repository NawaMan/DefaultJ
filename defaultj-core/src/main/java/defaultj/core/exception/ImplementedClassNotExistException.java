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
package defaultj.core.exception;

import static java.lang.String.format;

/**
 * This exception is thrown when the specified implementation cannot be found.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ImplementedClassNotExistException extends DefaultCreationException {
    
    private static final long serialVersionUID = 8158915955662317452L;
    
    private final String implementationClassName;
    
    /**
     * Constructs the exception without the root cause.
     * 
     * @param clazz                    the class.
     * @param implementationClassName  the implementation class.
     */
    public ImplementedClassNotExistException(Class<?> clazz, String implementationClassName) {
        this(clazz, implementationClassName, null);
    }
    
    /**
     * Constructs the exception with the root cause.
     * 
     * @param clazz                    the class.
     * @param implementationClassName  the implementation class.
     * @param cause                    the root cause.
     */
    public ImplementedClassNotExistException(Class<?> clazz, String implementationClassName, Throwable cause) {
        super(format("Unable to find or initialize the implementation ('%s') for '%s'",
                implementationClassName,
                clazz.getName()),
            clazz,
            cause);
        this.implementationClassName = implementationClassName;
    }
    
    /**
     * Returns the implementation class name.
     * 
     * @return the implementation class name.
     */
    public String getImplementationClassName() {
        return this.implementationClassName;
    }
    
}
