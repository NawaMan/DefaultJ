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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import defaultj.api.IProvideDefault;
import defaultj.core.bindings.FactoryBinding;
import lombok.val;

public class BindingTest {
    
    //== InstanceBinging ==
    
    @Test
    public void testInstanceBinding() {
        val expectedString = "I am a string.";
        
        val bindings = new Bindings.Builder().bind(String.class, expectedString).build();
        val provider = new DefaultProvider.Builder().bingings(bindings).build();
        val actual   = provider.get(String.class);
        assertEquals(expectedString, actual);
    }
    
    //== TypeBinging ==
    
    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Do nothing!");
        }
    }
    
    @Test
    public void testTypeBinding() {
        val expectedType = MyRunnable.class;
        
        val bindings = new Bindings.Builder().bind(Runnable.class, MyRunnable.class).build();
        val provider = new DefaultProvider.Builder().bingings(bindings).build();
        
        assertTrue(expectedType.isInstance(provider.get(Runnable.class)));
        assertTrue(expectedType.isInstance(provider.get(MyRunnable.class)));
    }
    
    //== FactoryBinging ==
    
    public static class IncrementalIntegerFactory implements ICreateDefault<Integer> {
        private AtomicInteger integer = new AtomicInteger(0);
        @Override
        public Integer create(IProvideDefault defaultProvider) {
            return integer.getAndIncrement();
        }
    }
    
    @Test
    public void testFactoryBinding() {
        val integerFactory = new IncrementalIntegerFactory();
        
        val factoryBinding = new FactoryBinding<>(integerFactory);
        val bindings       = new Bindings.Builder().bind(Integer.class, factoryBinding).build();
        val provider       = new DefaultProvider.Builder().bingings(bindings).build();
        
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
        val bindings = new Bindings.Builder().bind(Car.class, SuperCar.class).build();
        val provider = new DefaultProvider.Builder().bingings(bindings).build();
        
        assertEquals("SUPER FLASH!!!!", provider.get(Person.class).zoom());
    }
    
}
