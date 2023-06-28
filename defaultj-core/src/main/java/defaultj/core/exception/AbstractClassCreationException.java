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

import defaultj.api.ProvideDefaultException;

/**
 * This exception is thrown when there is an to create an instance of an abstract class .
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class AbstractClassCreationException extends ProvideDefaultException {

    private static final long serialVersionUID = 5751024581572983499L;

    /**
     * Constructor 
     * 
     * @param clazz  the class that this fail creation is attempted too.
     **/
    public AbstractClassCreationException(Class<?> clazz) {
        this("Abstract class can't be created: " + clazz, clazz);
    }
    

    /**
     * Constructor 
     * 
     * @param msg    the error message.
     * @param clazz  the class that this fail creation is attempted too.
     **/
    protected AbstractClassCreationException(String msg, Class<?> clazz) {
        super(msg, clazz, null);
    }
    
}
