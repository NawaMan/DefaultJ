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

import java.util.Optional;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import dssb.objectprovider.annotations.Nullable;
import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

@SuppressWarnings("javadoc")
@ExtensionMethod({ NullableJ.class })
public class ConstructorWithOptionalTest {
    
    private ObjectProvider provider = new ObjectProvider();
    
    //== Interface has no defaults so not filled when optional.
    
    public static interface Department {
        public String name();
    }
    
    @ExtensionMethod({ NullableJ.class })
    public static class Employee {
        private Department department;
        public Employee(Optional<Department> department) {
            this.department = department.orElse(null);
        }
        public String departmentName() {
            return department._whenNotNull().map(Department::name).orElse(null);
        }
    }
    
    @Test
    public void testThat_OptionalEmptyIsGivenIfTheValueCannotBeObtained() {
        assertNull(provider.get(Employee.class).departmentName());
    }
    
    //== Class has default, so attempt to fill that in.
    
    public static class Salary {
        public Salary() {
            throw new RuntimeException("Too much");
        }
    }
    
    @ExtensionMethod({ NullableJ.class })
    public static class Manager {
        private Optional<Salary> salary;
        public Manager(Optional<Salary> salary) {
            this.salary = salary;
        }
        public Optional<Salary> salary() {
            return salary;
        }
    }
    
    @Test
    public void testOptionalParameterWithValue() {
        assertNotNull(provider.get(Manager.class).salary());
    }
    
    @ExtensionMethod({ NullableJ.class })
    public static class ExecutiveNullable {
        private Salary salary;
        public ExecutiveNullable(@Nullable Salary salary) {
            this.salary = salary;
        }
        public Salary salary() {
            return salary;
        }
    }
    
    @ExtensionMethod({ NullableJ.class })
    public static class ExecutiveOptional {
        private Salary salary;
        public ExecutiveOptional(@dssb.objectprovider.annotations.Optional Salary salary) {
            this.salary = salary;
        }
        public Salary salary() {
            return salary;
        }
    }
    
    @Test
    public void testThat_nullIsGivenToNullableOrOptionalParameterIfTheValueCannotBeObtainedDueToException() {
        assertNull(provider.get(ExecutiveNullable.class).salary());
        assertNull(provider.get(ExecutiveOptional.class).salary());
    }
    
    @ExtensionMethod({ NullableJ.class })
    public static class OwnerNullable {
        private Optional<Salary> salary;
        public OwnerNullable(@Nullable Optional<Salary> salary) {
            this.salary = salary;
        }
        public Optional<Salary> salary() {
            return salary;
        }
    }
    
    @ExtensionMethod({ NullableJ.class })
    public static class OwnerOptional {
        private Optional<Salary> salary;
        public OwnerOptional(@dssb.objectprovider.annotations.Optional Optional<Salary> salary) {
            this.salary = salary;
        }
        public Optional<Salary> salary() {
            return salary;
        }
    }
    
    @Test
    public void testThat_nullIsGivenToNullableOptionalParameterIfTheValueCannotBeObtainedDueToException() {
        assertNull(provider.get(OwnerNullable.class).salary());
        assertNull(provider.get(OwnerOptional.class).salary());
    }
}
