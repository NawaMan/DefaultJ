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
package defaultj.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static nullablej.NullableJ._isNotNull;
import static nullablej.NullableJ._isNull;
import static nullablej.NullableJ._or;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import defaultj.api.IProvideDefault;
import defaultj.api.ProvideDefaultException;
import defaultj.core.exception.AbstractClassCreationException;
import defaultj.core.exception.CyclicDependencyDetectedException;
import defaultj.core.strategies.ConstructorSupplierFinder;
import defaultj.core.strategies.DefaultImplementationSupplierFinder;
import defaultj.core.strategies.DefaultInterfaceSupplierFinder;
import defaultj.core.strategies.EnumValueSupplierFinder;
import defaultj.core.strategies.FactoryMethodSupplierFinder;
import defaultj.core.strategies.IFindSupplier;
import defaultj.core.strategies.ImplementedBySupplierFinder;
import defaultj.core.strategies.NullSupplierFinder;
import defaultj.core.strategies.SingletonFieldFinder;
import defaultj.core.utils.failable.Failable.Supplier;
import lombok.val;
import nullablej.nullable.Nullable;
import nullablej.nullvalue.strategies.KnownNewNullValuesFinder;
import nullablej.nullvalue.strategies.KnownNullValuesFinder;

/**
 * DefaultProvider can provide defaults.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultProvider implements IProvideDefault, IFindSupplier {
    
    @SuppressWarnings("rawtypes")
    private static final Supplier NoSupplier = ()->null;
    
    
    private static final List<IFindSupplier> beforeAdditionalFinders = Arrays.asList(
            new DefaultImplementationSupplierFinder(),
            new ImplementedBySupplierFinder(),
            new EnumValueSupplierFinder(),
            new DefaultInterfaceSupplierFinder()
    );
    private static final List<IFindSupplier> afterAdditionalFinders = Arrays.asList(
            new SingletonFieldFinder(),
            new FactoryMethodSupplierFinder(),
            new NullSupplierFinder(),
            new ConstructorSupplierFinder()
    );
    
    private static final KnownNullValuesFinder    knownNullValuesFinder = new KnownNullValuesFinder();
    private static final KnownNewNullValuesFinder knownNewNullValuesFinder = new KnownNewNullValuesFinder();
    
    @SuppressWarnings("rawtypes")
    private static final ThreadLocal<Set<Class>> beingCreateds
            = ThreadLocal.withInitial(()->new HashSet<>());
    
    private static final List<IFindSupplier> noAdditionalSuppliers = emptyList();
    
    private static final Bindings noBinding = new Bindings.Builder().build();
    
    private IProvideDefault       parent;
    private List<IFindSupplier>   finders;
    private IHandleProvideFailure provideFailureHandler;
    
    private Bindings binidings;
    
    @SuppressWarnings("rawtypes")
    private Map<Class, Supplier> suppliers = new ConcurrentHashMap<Class, Supplier>();
    
    private List<IFindSupplier> additionalSupplierFinders;
    
    /** Ready to use instance with default settings */
    public static final DefaultProvider instance = new DefaultProvider();
    
    /**
     * Constructs the DefaultProvider without any configuration.
     **/
    public DefaultProvider() {
        this(null, null, null, null);
    }
    
    /**
     * Constructs the DefaultProvider with configurations.
     * @param parent                     the parent default provider.
     * @param additionalSupplierFinders  additional supplier finders.
     * @param bingings                   the bindings.
     * @param provideFailureHandler      the handler for provide failure.
     **/
    public DefaultProvider(
            IProvideDefault       parent,
            List<IFindSupplier>   additionalSupplierFinders,
            Bindings              bingings,
            IHandleProvideFailure provideFailureHandler) {
        this.parent                = parent;
        this.finders               = combineFinders(additionalSupplierFinders);
        this.provideFailureHandler = provideFailureHandler;
        this.binidings             = _or(bingings, noBinding);
        
        // Supportive
        this.additionalSupplierFinders = additionalSupplierFinders;
    }
    
    /**
     * Create a builder for the DefaultProvider.
     */
    public static class Builder {
        private IProvideDefault       parent;
        private List<IFindSupplier>   additionalSupplierFinders;
        private Bindings              bingings;
        private IHandleProvideFailure provideFailureHandler;
        
        /**
         * Constructs a builder with all default configurations.
         */
        public Builder() {
            this(null, null, null, null);
        }
        
        public Bindings bingings() {
            return bingings;
        }
        
        public Builder bingings(Bindings bingings) {
            this.bingings = bingings;
            return this;
        }
        
        public Builder(IProvideDefault       parent,
                       List<IFindSupplier>   additionalSupplierFinders,
                       Bindings              bingings,
                       IHandleProvideFailure provideFailureHandler) {
            super();
            this.parent                    = parent;
            this.additionalSupplierFinders = additionalSupplierFinders;
            this.bingings                  = bingings;
            this.provideFailureHandler     = provideFailureHandler;
        }
        
        /**
         * Build the DefaultProvider.
         * 
         * @return  the newly built DefaultProvider.
         */
        public DefaultProvider build() {
            return new DefaultProvider(parent, additionalSupplierFinders, bingings, provideFailureHandler);
        }
    }
    
    private static List<IFindSupplier> combineFinders(List<IFindSupplier> additionalSupplierFinders) {
        val finderList = new ArrayList<IFindSupplier>();
        finderList.addAll(beforeAdditionalFinders);
        finderList.addAll(_or(additionalSupplierFinders, noAdditionalSuppliers));
        finderList.addAll(afterAdditionalFinders);
        return unmodifiableList(finderList);
    }
    
    /**
     * Create a new provider with the given provide failure provider.
     * 
     * @param additionalSupplierFinders  the supplier finders.
     * @return  a new default provider with all configuration of this provider except for the additional supplier finders.
     */
    public DefaultProvider withAdditionalSupplier(List<IFindSupplier> additionalSupplierFinders) {
        return new DefaultProvider(parent, additionalSupplierFinders, binidings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given provide failure provider.
     * 
     * @param additionalSupplierFinders  the supplier finders.
     * @return  a new default provider with all configuration of this provider except for the additional supplier finders.
     */
    public DefaultProvider withAdditionalSupplier(Stream<IFindSupplier> additionalSupplierFinders) {
        val finders = additionalSupplierFinders.collect(toList());
        return new DefaultProvider(parent, finders, binidings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given provide failure provider.
     * 
     * @param additionalSupplierFinders  the supplier finders.
     * @return  a new default provider with all configuration of this provider except for the additional supplier finders.
     */
    public DefaultProvider withAdditionalSupplier(IFindSupplier ... additionalSupplierFinders) {
        val finders = asList(additionalSupplierFinders);
        return new DefaultProvider(parent, finders, binidings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given binding.
     * 
     * @param bindings  the bindings.
     * @return  a new default provider with all configuration of this provider except for the bindings.
     */
    public DefaultProvider withBindings(Bindings bindings) {
        return new DefaultProvider(parent, additionalSupplierFinders, bindings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given binding.
     * 
     * @param bindingBuilder  the bindings builder.
     * @return  a new default provider with all configuration of this provider except for the bindings.
     */
    public DefaultProvider withBindings(Bindings.Builder bindingBuilder) {
        val bindings = Optional.ofNullable(bindingBuilder)
                     .map   (Bindings.Builder::build)
                     .orElse(null);
        return new DefaultProvider(parent, additionalSupplierFinders, bindings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given binding.
     * 
     * @param <TYPE>  the type of the class to be bound with.
     * 
     * @param clzz     the binding class.
     * @param binding  the binding for the class.
     * @return  a new default provider with all configuration of this provider except for the bindings.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <TYPE> DefaultProvider withBinding(Class<TYPE> clzz, IBind<? extends TYPE> binding) {
        val bindingMap = new HashMap<Class<TYPE>, IBind<? extends TYPE>>();
        bindingMap.put(requireNonNull(clzz), requireNonNull(binding));
        
        val bindings = new Bindings((Map<Class, IBind>)(Map)bindingMap);
        return new DefaultProvider(parent, additionalSupplierFinders, bindings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given binding.
     * 
     * @param <TYPE>  the type of the class to be bound with.
     * 
     * @param clzz      the binding class.
     * @param instance  the bound instance.
     * @return  a new default provider with all configuration of this provider except for the bindings.
     */
    public <TYPE> DefaultProvider withBinding(Class<TYPE> clzz, TYPE instance) {
        val bindings = new Bindings.Builder().bind(requireNonNull(clzz), instance).build();
        return new DefaultProvider(parent, additionalSupplierFinders, bindings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given binding.
     * 
     * @param <TYPE>  the type of the class to be bound with.
     * 
     * @param clzz       the binding class.
     * @param boundClzz  the bound class.
     * @return  a new default provider with all configuration of this provider except for the bindings.
     */
    public <TYPE> DefaultProvider withBinding(Class<TYPE> clzz, Class<? extends TYPE> boundClzz) {
        val bindings = new Bindings.Builder().bind(requireNonNull(clzz), boundClzz).build();
        return new DefaultProvider(parent, additionalSupplierFinders, bindings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given provide failure provider.
     * 
     * @param provideFailureHandler  the handler.
     * @return  a new default provider with all configuration of this provider except for the handler.
     */
    public DefaultProvider withProvideFailureHandler(IHandleProvideFailure provideFailureHandler) {
        return new DefaultProvider(parent, additionalSupplierFinders, binidings, provideFailureHandler);
    }
    
    /**
     * Create an instance of the given class.
     * 
     * @param theGivenClass  the data class.
     * @return the created value.
     * @throws ProvideDefaultException when there is a problem providing the default.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public <TYPE> TYPE get(Class<TYPE> theGivenClass) throws ProvideDefaultException {
        val set = beingCreateds.get();
        if (set.contains(theGivenClass))
            throw new CyclicDependencyDetectedException(theGivenClass);
        
        try {
            set.add(theGivenClass);
            
            try {
                val supplier = getSupplierFor(theGivenClass);
                val instance = supplier.get();
                return theGivenClass.isPrimitive() ? instance : theGivenClass.cast(instance);
            } catch (ProvideDefaultException e) {
                throw e;
            } catch (Throwable e) {
                throw new ProvideDefaultException(theGivenClass, e);
            }
        } finally {
            set.remove(theGivenClass);
        }
    }
    
    /**
     * Returns the default for the given class as Nullable.
     * 
     * @param <TYPE>  the data type represented by the given class.
     * @param theGivenClass
     *          the given class.
     * @return  the default associated with the given class.
     * @throws ProvideDefaultException  if there is a problem getting the default.
     */
    public <TYPE> Nullable<TYPE> nullable(Class<TYPE> theGivenClass)
            throws ProvideDefaultException {
        return Nullable.of(get(theGivenClass));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> getSupplierFor(
            Class<TYPE> theGivenClass) {
        
        Supplier supplier = suppliers.get(theGivenClass);
        if (_isNull(supplier)) {
            supplier = newSupplierFor(theGivenClass);
            supplier = _or(supplier, NoSupplier);
            suppliers.put(theGivenClass, supplier);
        }
        return supplier;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE>
            find(Class<TYPE> theGivenClass, IProvideDefault defaultProvider) {
        val supplier = getSupplierFor(theGivenClass);
        return (supplier != null)
                ? (Supplier)supplier
                : (defaultProvider instanceof DefaultProvider)
                    ? ((DefaultProvider)defaultProvider).getSupplierFor(theGivenClass)
                    : (defaultProvider != null)
                        ? () -> defaultProvider.get(theGivenClass)
                        : null;
    }
    
    @SuppressWarnings({ "rawtypes" })
    private <T> Supplier newSupplierFor(Class<T> theGivenClass) {
        val binding = this.binidings.getBinding(theGivenClass);
        if (_isNotNull(binding))
            return ()->binding.get(this);
        
        if (DefaultProvider.class.isAssignableFrom(theGivenClass))
            return ()->this;
        
        val parentProvider = (IProvideDefault)_or(this.parent, this);
        for (val finder : finders) {
            val supplier = finder.find(theGivenClass, parentProvider);
            if (_isNotNull(supplier))
                return supplier;
        }
        
        if (IProvideDefault.class.isAssignableFrom(theGivenClass))
            return ()->this;
        
            val knownValue = knownNullValuesFinder.findNullValueOf(theGivenClass);
        if (knownValue != null)
            return ()->knownValue;
            
        if (knownNewNullValuesFinder.canFindFor(theGivenClass))
            return ()->knownNewNullValuesFinder.findNullValueOf(theGivenClass);
        
        return ()->handleLoadingFailure(theGivenClass);
    }
    
    private <T> Object handleLoadingFailure(Class<T> theGivenClass) {
        if (_isNotNull(this.provideFailureHandler)) {
            return callHandler(theGivenClass);
        } else {
            return defaultHandling(theGivenClass);
        }
    }
    private <T> Object callHandler(Class<T> theGivenClass) {
        T value = this.provideFailureHandler.handle(theGivenClass);
        return value;
    }
    private <T> Object defaultHandling(Class<T> theGivenClass) {
        if (theGivenClass.isInterface()
         || Modifier.isAbstract(theGivenClass.getModifiers()))
            throw new AbstractClassCreationException(theGivenClass);
        
        return null;
    }
    
}
