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
package defaultj.api;

import java.util.Optional;

import lombok.val;

/**
 * Classes implementing this interface can provide a default given a class..
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@FunctionalInterface
public interface IProvideDefault {
    
    /**
     * Returns the default for the given class.
     * 
     * @param <TYPE>  the data type represented by the given class.
     * @param theGivenClass
     *          the given class.
     * @return  the default associated with the given class.
     * @throws ProvideDefaultException  if there is a problem getting the default.
     */
    public <TYPE> TYPE get(Class<TYPE> theGivenClass)
            throws ProvideDefaultException;
    
    /**
     * Returns the default for the given class as Optional.
     * 
     * @param <TYPE>  the data type represented by the given class.
     * @param theGivenClass
     *          the given class.
     * @return  the default associated with the given class.
     * @throws ProvideDefaultException  if there is a problem getting the default.
     */
    public default <TYPE> Optional<TYPE> optional(Class<TYPE> theGivenClass)
            throws ProvideDefaultException {
        val defaultValue = get(theGivenClass);
        return Optional.ofNullable(defaultValue);
    }
    
    // == Factory method ==
    
    /** The property that contains the class name of the class of the implementation. */
    public static final String implementationClassNameProperty = "DefaultProviderClassName";
    
    /** The property that contains the suggest class name of the class of the implementation. */
    public static final String suggestImplementationClassNameProperty = "SuggestDefaultProviderClassName";
    
    /** The name of the class of the implementation. */
    public static final String implementationClassName = "defaultj.core.DefaultProvider";
    
    /** The flag to fall back to basic default provider (only do default constructor) when none is found - default to true. */
    public static final String fallbackToBasicDefaultProvider = "FallbackToBasicDefaultProvider";
    
    /**
     * Attempt to find the default provider.
     * 
     * This is done by looking for the class DefaultProvider from the core package by name.
     * If the class is not in the classpath. This method will return null.
     * 
     * @return the Optional value of the DefaultProvider if the class was in the classpath.
     */
    public static Optional<IProvideDefault> defaultProvider() {
        return utils.getDefault();
    }
    
    
}
