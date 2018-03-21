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

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dssb.objectprovider.api.IProvideObject;
import dssb.objectprovider.impl.bindings.FactoryBinding;
import dssb.objectprovider.impl.bindings.InstanceBinding;
import dssb.objectprovider.impl.bindings.TypeBinding;
import lombok.val;

@SuppressWarnings("javadoc")
public class BindingTest {
    
    //== InstanceBinging ==
    
    @Test
    public void testInstanceBinding() {
        val expectedString = "I am a string.";
        
        val instanceBinding = new InstanceBinding<>(expectedString);
        val bindings        = new Bindings.Builder().bind(String.class, instanceBinding).build();
        val provider        = new ObjectProvider.Builder().bingings(bindings).build();
        
        assertEquals(expectedString, provider.get(String.class));
    }
    
    //== TypeBinging ==
    
    public static class MyRunnable implements Runnable {
        @Override
        public void run() {}
    }
    
    @Test
    public void testTypeBinding() {
        val expectedClass = MyRunnable.class;
        
        val typeBinding = new TypeBinding<Runnable>(MyRunnable.class);
        val bindings    = new Bindings.Builder().bind(Runnable.class, typeBinding).build();
        val provider    = new ObjectProvider.Builder().bingings(bindings).build();
        
        assertTrue(expectedClass.isInstance(provider.get(Runnable.class)));
        assertTrue(expectedClass.isInstance(provider.get(MyRunnable.class)));
    }
    
    //== FactoryBinging ==
    
    public static class IncrementalIntegerFactory implements ICreateObject<Integer> {
        private AtomicInteger integer = new AtomicInteger(0);
        @Override
        public Integer create(IProvideObject objectprovider) {
            return integer.getAndIncrement();
        }
        
    }
    
    @Test
    public void testFactoryBinding() {
        val integerFactory = new IncrementalIntegerFactory();
        
        val factoryBinding = new FactoryBinding<>(integerFactory);
        val bindings       = new Bindings.Builder().bind(Integer.class, factoryBinding).build();
        val provider       = new ObjectProvider.Builder().bingings(bindings).build();
        
        assertTrue(0 == provider.get(Integer.class));
        assertTrue(1 == provider.get(Integer.class));
        assertTrue(2 == provider.get(Integer.class));
    }
    
    // == Bind use in the dependency ==
    
    public static class Car {
        public String zoom() {
            return "FLASH!";
        }
    }
    
    public static class SuperCar extends Car {
        public String zoom() {
            return "SUPER FLASH!!!!";
        }
    }
    
    public static class Person {
        private Car car;
        public Person(Car car) {
            this.car = car;
        }
        public String zoom() {
            return (car != null) ? car.zoom() : "Meh";
        }
    }
    
    @Test
    public void testBindingAsDependency() {
        val typeBinding = new TypeBinding<Car>(SuperCar.class);
        val bindings    = new Bindings.Builder().bind(Car.class, typeBinding).build();
        val provider    = new ObjectProvider.Builder().bingings(bindings).build();
        
        assertEquals("SUPER FLASH!!!!", provider.get(Person.class).zoom());
    }
    
}
