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
package defaultj.core.bindings;

import defaultj.api.IProvideDefault;
import defaultj.core.IBind;
import defaultj.core.ICreateDefault;
import lombok.val;

/**
 * Binding to a factory.
 *
 * @param <TYPE> the type the factory can provide.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class FactoryBinding<TYPE> implements IBind<TYPE> {
    
    private final ICreateDefault<TYPE> factory;
    
    /**
     * Constructs a factory binding using the the factory.
     * 
     * @param factory  the factory to create the default.
     */
    public FactoryBinding(ICreateDefault<TYPE> factory) {
        this.factory = factory;
    }
    
    @Override
    public TYPE get(IProvideDefault defaultProvider) {
        val value = this.factory.create(defaultProvider);
        return value;
    }
    
}
