//  ========================================================================
//  Copyright (c) 2017 Direct Solution Software Builders (DSSB).
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
package dssb.objectprovider.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import dssb.objectprovider.api.IProvideObject;
import dssb.objectprovider.api.ProvideObjectException;
import dssb.objectprovider.impl.exception.AbstractClassCreationException;
import dssb.objectprovider.impl.exception.CyclicDependencyDetectedException;
import dssb.objectprovider.impl.exception.ObjectCreationException;
import dssb.objectprovider.impl.strategies.ConstructorSupplierFinder;
import dssb.objectprovider.impl.strategies.DefaultInterfaceSupplierFinder;
import dssb.objectprovider.impl.strategies.EnumValueSupplierFinder;
import dssb.objectprovider.impl.strategies.FactoryMethodSupplierFinder;
import dssb.objectprovider.impl.strategies.IFindSupplier;
import dssb.objectprovider.impl.strategies.DefaultImplementationSupplierFinder;
import dssb.objectprovider.impl.strategies.NullSupplierFinder;
import dssb.objectprovider.impl.strategies.SingletonFieldFinder;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.NullableJ;

/**
 * ObjectProvider can provide objects.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@ExtensionMethod({ NullableJ.class })
public class ObjectProvider implements IProvideObject {
    
    // Stepping stone
//    public static final ObjectProvider instance = new ObjectProvider();
    
    // TODO - Add default factory.
    // TODO - Should create interface with all default method.
    
    @SuppressWarnings("rawtypes")
    private static final Supplier NoSupplier = ()->null;
    
    
    private static final List<IFindSupplier> classLevelfinders = Arrays.asList(
            new DefaultImplementationSupplierFinder(),
            new NullSupplierFinder(),
            new EnumValueSupplierFinder(),
            new DefaultInterfaceSupplierFinder()
    );
    private static final List<IFindSupplier> elementLevelfinders = Arrays.asList(
            new SingletonFieldFinder(),
            new FactoryMethodSupplierFinder(),
            new ConstructorSupplierFinder()
    );
    
    @SuppressWarnings("rawtypes")
    private static final ThreadLocal<Set<Class>> beingCreateds
            = ThreadLocal.withInitial(()->new HashSet<>());
    
    private static final List<IFindSupplier> noAdditionalSuppliers = emptyList();
    
    private static final Bindings noBinding = new Bindings.Builder().build();
    
    private IProvideObject        parent;
    private List<IFindSupplier>   finders;
    private IHandleProvideFailure provideFailureHandler;
    
    private Bindings binidings;
    
    @SuppressWarnings("rawtypes")
    private Map<Class, Supplier> suppliers = new ConcurrentHashMap<Class, Supplier>();
    
    private List<IFindSupplier>  additionalSupplierFinders;
    
    /**
     * Constructs the ObjectProvider without any configuration.
     **/
    public ObjectProvider() {
        this(null, null, null, null);
    }

    /**
     * Constructs the ObjectProvider with configurations.
     * @param parent                     the parent Object provider.
     * @param additionalSupplierFinders  additional supplier finders.
     * @param bingings                   the bindings.
     * @param provideFailureHandler      the handler for provide failure.
     **/
    public ObjectProvider(
            IProvideObject        parent,
            List<IFindSupplier>   additionalSupplierFinders,
            Bindings              bingings,
            IHandleProvideFailure provideFailureHandler) {
        this.parent                = parent;
        this.finders               = combineFinders(additionalSupplierFinders);
        this.provideFailureHandler = provideFailureHandler;
        this.binidings             = bingings._or(noBinding);
        
        // Supportive
        this.additionalSupplierFinders = additionalSupplierFinders;
    }
    
    /**
     * Create a builder for the ObjectProvider.
     */
    @Setter
    @AllArgsConstructor
    @Accessors(fluent=true,chain=true)
    public static class Builder {
        private IProvideObject        parent;
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
         * Build the ObjectProider.
         * 
         * @return  the newly built ObjectProvider.
         */
        public ObjectProvider build() {
            return new ObjectProvider(parent, additionalSupplierFinders, bingings, provideFailureHandler);
        }
    }
    
    private static List<IFindSupplier> combineFinders(List<IFindSupplier> additionalSupplierFinders) {
        val finderList = new ArrayList<IFindSupplier>();
        finderList.addAll(classLevelfinders);
        finderList.addAll(additionalSupplierFinders._or(noAdditionalSuppliers));
        finderList.addAll(elementLevelfinders);
        return unmodifiableList(finderList);
    }
    
    /**
     * Create a new provider with the given provide failure provider.
     * 
     * @param provideFailureHandler  the handler.
     * @return  a new object provider with all configuration of this provider except for the handler.
     */
    public ObjectProvider wihtProvideFailureHandler(IHandleProvideFailure provideFailureHandler) {
        return new ObjectProvider(parent, additionalSupplierFinders, binidings, provideFailureHandler);
    }
    
    /**
     * Create a new provider with the given provide failure provider.
     * 
     * @param bindings  the provision bindings.
     * @return  a new object provider with all configuration of this provider except for the bindings.
     */
    public ObjectProvider wihtBindings(Bindings bindings) {
        return new ObjectProvider(parent, additionalSupplierFinders, bindings, provideFailureHandler);
    }
    
    /**
     * Create an instance of the given class.
     * 
     * @param theGivenClass  the data class.
     * @return the created value.
     * @throws ProvideObjectException when there is a problem providing the object.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public <TYPE> TYPE get(Class<TYPE> theGivenClass) throws ProvideObjectException {
        val set = beingCreateds.get();
        if (set.contains(theGivenClass))
            throw new CyclicDependencyDetectedException(theGivenClass);
        
        try {
            set.add(theGivenClass);
            
            try {
                val supplier = getSupplierFor(theGivenClass);
                val instance = supplier.get();
                return theGivenClass.cast(instance);
            } catch (ObjectCreationException e) {
                throw e;
            } catch (Throwable e) {
                throw new ObjectCreationException(theGivenClass, e);
            }
        } finally {
            set.remove(theGivenClass);
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> getSupplierFor(
            Class<TYPE> theGivenClass) {
        
        Supplier supplier = suppliers.get(theGivenClass);
        if (supplier._isNull()) {
            supplier = newSupplierFor(theGivenClass);
            supplier = supplier._or(NoSupplier);
            suppliers.put(theGivenClass, supplier);
        }
        return supplier;
    }
    
    @SuppressWarnings({ "rawtypes" })
    private <T> Supplier newSupplierFor(Class<T> theGivenClass) {
        val binding = this.binidings.getBinding(theGivenClass);
        if (binding._isNotNull())
            return ()->binding.get(this);
        
        if (ObjectProvider.class.isAssignableFrom(theGivenClass))
            return ()->this;
        
        val parentProvider = (IProvideObject)this.parent._or(this);
        for (val finder : finders) {
            val supplier = finder.find(theGivenClass, parentProvider);
            if (supplier._isNotNull())
                return supplier;
        }
        
        if (IProvideObject.class.isAssignableFrom(theGivenClass))
            return ()->this;
        
        return ()->handleLoadingFailure(theGivenClass);
    }
    
    private <T> Object handleLoadingFailure(Class<T> theGivenClass) {
        if (this.provideFailureHandler._isNotNull()) {
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
