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
package nawaman.defaultj.core;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ConstructorTest {
    
    private DefaultProvider provider = new DefaultProvider();
    
    //== Regular class ==
    
    public static class Car {
        public String zoom() {
            return "FLASH!";
        }
    }
    
    @Test
    public void testRegularClass_useDefaultConstructorToProvideTheValue() {
        assertEquals("FLASH!",  provider.get(Car.class).zoom());
    }
    
    // == More Dependent class ==
    
    public static class Driver {
        private Car car;
        public Driver(Car car) {
            this.car = car;
        }
        public String zoom() {
            return car.zoom();
        }
    }
    
    @Test
    public void testOnlyConstructorIsTheDefaultConstructor() {
        assertEquals("FLASH!", provider.get(Driver.class).zoom());
    }
    
    @Test
    public void testDependendClass_theProviderFigureTheDependenct() {
        assertEquals("FLASH!", provider.get(Driver.class).zoom());
    }
    
    //== Multiple Constructor
    
    public static class Person {
        private Car car;
        public Person() {
            this(null);
        }
        public Person(Car car) {
            this.car = car;
        }
        public String zoom() {
            return (car != null) ? car.zoom() : "Meh";
        }
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals("Meh", provider.get(Person.class).zoom());
    }
    
    //== Injected Constructor ==
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {
        
    }
    
    public static class AnotherPerson {
        private Car car;
        public AnotherPerson() {
            this(null);
        }
        @Default
        public AnotherPerson(Car car) {
            this.car = car;
        }
        public String zoom() {
            return (car != null) ? car.zoom() : "Meh";
        }
    }
    
    @Test
    public void testInjectConstructor() {
        assertEquals("FLASH!", provider.get(AnotherPerson.class).zoom());
    }
    
    //== Post Constructor ==
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PostConstruct {}
    
    public static class ActivePerson {
        
        private String value = "Uninitialized";
        
        @PostConstruct
        private void init() {
            value = "Initialized!";
        }
        
        public String getValue() {
            return value;
        }
        
    }
    
    @Test
    public void testPostConstruct() {
        assertEquals("Initialized!", provider.get(ActivePerson.class).getValue());
    }
}
