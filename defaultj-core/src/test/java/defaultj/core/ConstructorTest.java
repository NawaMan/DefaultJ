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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

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
    
    //== Multiple Annotated Constructors ==
    
    public static class YetAnotherPerson {
        private Car car;
        @Default
        public YetAnotherPerson() {
            this(new Car() {
                @Override
                public String zoom() {
                    return "Zoom zoom ...";
                }
            });
        }
        @Default
        public YetAnotherPerson(Car car) {
            this.car = car;
        }
        public String zoom() {
            return (car != null) ? car.zoom() : "Meh";
        }
    }
    
    @Test
    public void testMultipleInjectConstructor() {
        assertEquals("Zoom zoom ...", provider.get(YetAnotherPerson.class).zoom());
    }
    
    //== Multiple Constructors WithNoArgsConstructor ==
    
    public static class PersonWithNoArgsConstructor {
        private Car car;
        public PersonWithNoArgsConstructor() {
            this(new Car() {
                @Override
                public String zoom() {
                    return "Zoom zoom ...";
                }
            });
        }
        public PersonWithNoArgsConstructor(Car car) {
            this.car = car;
        }
        public String zoom() {
            return (car != null) ? car.zoom() : "Meh";
        }
    }
    
    @Test
    public void testMultipleConstructorWithNoArgs() {
        assertEquals("Zoom zoom ...", provider.get(PersonWithNoArgsConstructor.class).zoom());
    }
    
    //== Multiple Constructors WithMultipleConstructors ==
    
    public static class PersonWithMultipleConstructors {
        private Car car;
        public PersonWithMultipleConstructors(String zoom) {
            this(new Car() {
                @Override
                public String zoom() {
                    return "zoom";
                }
            });
        }
        public PersonWithMultipleConstructors(Car car) {
            this.car = car;
        }
        public String zoom() {
            return (car != null) ? car.zoom() : "Meh";
        }
    }
    
    @Test
    public void testMultipleConstructors() {
        // Multiple constructors without `@Default`.
        assertEquals(null, provider.get(PersonWithMultipleConstructors.class));
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
