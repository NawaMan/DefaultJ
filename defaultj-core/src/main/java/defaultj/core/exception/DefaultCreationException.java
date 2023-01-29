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

import defaultj.api.ProvideDefaultException;

/**
 * This exception is thrown when creating a default fail.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultCreationException extends ProvideDefaultException {
    
    private static final long serialVersionUID = 5414890542605369904L;
    
    /**
     * Constructor 
     * 
     * @param clazz  the class that this fail creation is attempted too.
     **/
    public DefaultCreationException(Class<?> clazz) {
        this(clazz, null);
    }
    
    /**
     * Constructor 
     * 
     * @param clazz  the class that cause this exception.
     * @param cause  the cause exception of this exception.
     **/
    public DefaultCreationException(Class<?> clazz, Throwable cause) {
        super(clazz, cause);
    }
    
    /**
     * Constructor 
     * 
     * @param msg    the error message.
     * @param clazz  the class that this fail creation is attempted too.
     * @param cause  the root cause of the problem.
     **/
    protected DefaultCreationException(String msg, Class<?> clazz, Throwable cause) {
        super(msg, clazz, cause);
    }
    
}
