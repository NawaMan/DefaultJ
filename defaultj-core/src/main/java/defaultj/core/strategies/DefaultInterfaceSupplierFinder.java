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
package defaultj.core.strategies;

import static defaultj.core.utils.AnnotationUtils.has;
import static nullablej.utils.reflection.UProxy.createDefaultProxy;
import static nullablej.utils.reflection.UProxy.getNonDefaultMethods;

import defaultj.annotations.DefaultInterface;
import defaultj.api.IProvideDefault;
import defaultj.core.exception.NonDefaultInterfaceException;
import defaultj.core.utils.failable.Failable.Supplier;
import lombok.val;

/**
 * This class find default of an interface with all default methods.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultInterfaceSupplierFinder implements IFindSupplier {

    private static final String DEFAULT_INTERFACE = DefaultInterface.class.getSimpleName();
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        boolean isDefaultInterface
                =  theGivenClass.isInterface()
                && has(theGivenClass.getAnnotations(), DEFAULT_INTERFACE);
        
        if (!isDefaultInterface)
            return null;
        
        val nonDefaults = getNonDefaultMethods(theGivenClass);
        if (!nonDefaults.isEmpty()) {
            throw new NonDefaultInterfaceException(theGivenClass, nonDefaults);
        }
        
        val theProxy = createDefaultProxy(theGivenClass);
        return () -> theProxy;
    }
    
}
