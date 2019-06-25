//  ========================================================================
//  Copyright (c) 2017-2018 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package nawaman.defaultj.core;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static nullablej.NullableJ._isNotNull;
import static nullablej.NullableJ._isNull;
import static nullablej.NullableJ._or;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.api.ProvideDefaultException;
import nawaman.defaultj.core.exception.AbstractClassCreationException;
import nawaman.defaultj.core.exception.CyclicDependencyDetectedException;
import nawaman.defaultj.core.strategies.ConstructorSupplierFinder;
import nawaman.defaultj.core.strategies.DefaultImplementationSupplierFinder;
import nawaman.defaultj.core.strategies.DefaultInterfaceSupplierFinder;
import nawaman.defaultj.core.strategies.EnumValueSupplierFinder;
import nawaman.defaultj.core.strategies.FactoryMethodSupplierFinder;
import nawaman.defaultj.core.strategies.IFindSupplier;
import nawaman.defaultj.core.strategies.ImplementedBySupplierFinder;
import nawaman.defaultj.core.strategies.NullSupplierFinder;
import nawaman.defaultj.core.strategies.SingletonFieldFinder;
import nawaman.failable.Failable.Supplier;
import nullablej.nullvalue.strategies.KnownNewNullValuesFinder;
import nullablej.nullvalue.strategies.KnownNullValuesFinder;

/**
 * DefaultProvider can provide defaults.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultProvider implements IProvideDefault {
    
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
    @Setter
    @AllArgsConstructor
    @Accessors(fluent=true,chain=true)
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
     * @param provideFailureHandler  the handler.
     * @return  a new default provider with all configuration of this provider except for the handler.
     */
    public DefaultProvider wihtProvideFailureHandler(IHandleProvideFailure provideFailureHandler) {
        return new DefaultProvider(parent, additionalSupplierFinders, binidings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given provide failure provider.
     * 
     * @param bindings  the provision bindings.
     * @return  a new default provider with all configuration of this provider except for the bindings.
     */
    public DefaultProvider wihtBindings(Bindings bindings) {
        return new DefaultProvider(parent, additionalSupplierFinders, bindings, provideFailureHandler);
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
//        if (theGivenClass.isPrimitive()) {
//            val knownValue = knownNullValuesFinder.findNullValueOf(theGivenClass);
//            if (knownValue != null)
//                return knownValue;
//        }
        
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
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
