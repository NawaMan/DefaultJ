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
package defaultj.api;

/**
 * Exception thrown if there is a problem while trying to provide a default.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ProvideDefaultException extends RuntimeException {
    
    private static final long serialVersionUID = -1503175854525324555L;
    
    private final Class<?> clazz;
    
    /**
     * Constructor the exception with the class that was trying to provide.
     * 
     * @param clazz
     *          the class that this fail provision is attempted too.
     **/
    public ProvideDefaultException(Class<?> clazz) {
        this(clazz, null);
    }
    
    /**
     * Constructor the exception with the class that was trying to provide and the cause of the problem.
     * 
     * @param clazz 
     *          the class that this fail provision is attempted too.
     * @param cause
     *          the cause of the problem.
     **/
    public ProvideDefaultException(Class<?> clazz, Throwable cause) {
        super(clazz.getCanonicalName(), cause);
        this.clazz = clazz;
    }
    /**
     * Constructor 
     * 
     * @param msg    the error message.
     * @param clazz  the class that this fail creation is attempted too.
     * @param cause  the root cause of the problem.
     **/
    protected ProvideDefaultException(String msg, Class<?> clazz, Throwable cause) {
        super(msg, cause);
        this.clazz = clazz;
    }
    
    /**
     * Returns the target class.
     * 
     * @return the target class with the problem.
     **/
    public Class<?> getTargetClass() {
        return clazz;
    }
    
}
