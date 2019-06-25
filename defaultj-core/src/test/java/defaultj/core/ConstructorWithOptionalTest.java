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
package defaultj.core;

import java.util.Optional;

import org.junit.Test;

import defaultj.annotations.Nullable;
import defaultj.core.DefaultProvider;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import lombok.experimental.ExtensionMethod;
import nullablej.NullableJ;

@SuppressWarnings("javadoc")
@ExtensionMethod({ NullableJ.class })
public class ConstructorWithOptionalTest {
    
    private DefaultProvider provider = new DefaultProvider();
    
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
            throw new IllegalStateException("Too much");
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
        public ExecutiveOptional(@defaultj.annotations.Optional Salary salary) {
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
        public OwnerOptional(@defaultj.annotations.Optional Optional<Salary> salary) {
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
